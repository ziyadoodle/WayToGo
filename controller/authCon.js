const admin = require('firebase-admin')
const { signInWithEmailAndPassword } = require('@firebase/auth')
const { auth } = require('../config/firebase')
const jwt = require('jsonwebtoken')
const nodemailer = require('nodemailer')

//need attention: body pake format x-www-urlencoded. kalo pake raw providernya jd anonymous jd gabisa login
//================================================================================================================
const signUp = async (req, res) => {
    try {
        const { displayName, email, password } = req.body;

        const userRecord = await admin.auth().createUser({
            displayName,
            email,
            password,
        })

        const verificationLink = await admin.auth().generateEmailVerificationLink(email);

        var transporter = nodemailer.createTransport({
            host: "smtp.gmail.com",
            auth: {
                user: "waytogojabar@gmail.com",
                pass: "dwnzsmmoyqnjesjt"
            }
        });

        const name = userRecord.displayName
        const mailOptions = {
            from: "waytogo@gmail.com",
            to: email,
            subject: "Verify Your Email",
            html: 
            `<p> Hello ${name}, Welcome to WayToGo! </p>
            <p> Please verify your email by click the following link: <a href="${verificationLink}">${verificationLink}</a>.</p>
            <p> If you did not request verification for this address, you can ignore this email.</p>
            <p> Thank you, </p>
            <p> Your WayToGo team. </p>`,
        }
    
        transporter.sendMail(mailOptions, (error, info) => {
            if (error) {
            console.error("Error sending email:", error);
            res.status(500).json("Error sending email.");
            } else {
            console.log("Email sent:", info.response);
            res.status(200).json({ message: "Sign Up successful. Please check your email.", user: userRecord });
            }
        });
    } catch (error) {
        console.error(error);
        res.status(500).json("Sign Up failed.");
    }
};


//Sign In function
const signIn = async (req, res) => {
    try {
        console.log(req.body)
        const user = {
            email: req.body.email,
            password: req.body.password
        }

        if (!user.email || !user.password) {
            return res.status(400).json({ error: 'All field is required' })
        }

        const userCredential = await signInWithEmailAndPassword(auth, user.email, user.password)
        const userRecord = userCredential.user

        if (!userRecord.emailVerified) {
        return res.status(401).json({ error: 'Email not verified. Please check your email for verification instructions.' });
        }

        // Create a JWT token
        const token = jwt.sign({ id: userRecord.uid }, process.env.SECRET_KEY)

        res.status(200).json({ message: "Sign In successful", user: userRecord, token })
    } catch (error) {
        console.error('Error logging in user:', error)
        res.status(500).json({ error: 'Internal Server Error' })
    }
}

// Sign Out function
const signOut = async (req, res) => {
    try {
        await auth.signOut()

        res.status(200).json({ message: 'Sign Out successful' })
    } catch (error) {
        console.error('Error logging out user:', error)
        res.status(500).json({ error: 'Internal Server Error' })
    }
}

//Get User Profile function
const getUserProfile = async (req, res) => {
    try {
        // Get user User ID from the decoded token
        const decodedToken = req.user

        if (!decodedToken || !decodedToken.id) {
            return res.status(401).json({ error: 'Invalid token or missing UID' })
        }

        // Retrieve user information from Firebase Authentication
        const userRecord = await admin.auth().getUser(decodedToken.id)

        const userProfile = {
            uid: userRecord.uid,
            email: userRecord.email,
            displayName: userRecord.displayName
        }

        res.status(200).json({ user: userProfile })
    } catch (error) {
        console.error('Error retrieving user profile:', error)
        res.status(500).json({ error: 'Internal Server Error' })
    }
}

module.exports = {signUp, signIn, signOut, getUserProfile}
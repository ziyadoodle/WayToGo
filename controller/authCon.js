const admin = require('firebase-admin')
const { signInWithEmailAndPassword } = require('@firebase/auth')
const { auth } = require('../config/firebase')
const jwt = require('jsonwebtoken')
const nodemailer = require('nodemailer')

//================================================================================================================
const signUp = async (req, res) => {
    try {
        const { username, email, password } = req.body;

        if (!username || !email || !password) {
            return res.status(400).json({ message: "All fields are required" });
        }

        if (password.length < 8) {
            return res.status(400).json({ message: "Password must be at least 8 characters long" });
        }

        const userExists = await admin.auth().getUserByEmail(email).then(() => true).catch(() => false);

        if (userExists) {
            return res.status(400).json({ message: "Email is already in use" });
        }
        

        const userRecord = await admin.auth().createUser({
            displayName: username,
            email,
            password,
        })


        const verificationLink = await admin.auth().generateEmailVerificationLink(email);

        var transporter = nodemailer.createTransport({
            host: "smtp.gmail.com",
            auth: {
                user: "waytogojabar@gmail.com",
                pass: "osds ycdn lqxd snqw"
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
                admin.auth().deleteUser(userRecord.uid).catch(deleteError => {
                    console.error("Error deleting user:", deleteError);
                });
                res.status(500).json({massage:'Error sending email.'});
            } else {
                console.log("Email sent:", info.response);
                res.status(200).json({ message: "Sign Up successful. Please check your email.", user: userRecord });
            }
        });
    } catch (error) {
        console.error(error);

        if (userRecord) {
            await admin.auth().deleteUser(userRecord.uid);
        }

        res.status(500).json({massage:"Sign Up failed. " + error});
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
            return res.status(400).json({ massage: 'All field is required' })
        }

        const userCredential = await signInWithEmailAndPassword(auth, user.email, user.password)
        const userRecord = userCredential.user

        if (!userRecord.emailVerified) {
        return res.status(401).json({ massage: 'Email not verified. Please check your email for verification instructions.' });
        }

        // Create a JWT token
        const token = jwt.sign({ id: userRecord.uid }, process.env.SECRET_KEY)

        res.status(200).json({ message: "Sign In successful", user: userRecord, token })
    } catch (error) {
        console.error('Error logging in user:', error)
        res.status(500).json({ massage: 'Error during signin' })
    }
}

// Sign Out function
const signOut = async (req, res) => {
    try {
        await auth.signOut()

        res.status(200).json({ message: 'Sign Out successful' })
    } catch (error) {
        console.error('Error logging out user:', error)
        res.status(500).json({ massage: 'Internal Server Error' })
    }
}

//Get User Profile function
const getUserProfile = async (req, res) => {
    try {
        // Get user User ID from the decoded token
        const decodedToken = req.user

        if (!decodedToken || !decodedToken.id) {
            return res.status(401).json({ massage: 'Invalid token or missing UID' })
        }

        // Retrieve user information from Firebase Authentication
        const userRecord = await admin.auth().getUser(decodedToken.id)

        const userProfile = {
            uid: userRecord.uid,
            username: userRecord.displayName,
            email: userRecord.email
        }

        res.status(200).json({ user: userProfile })
    } catch (error) {
        console.error('Error retrieving user profile:', error)
        res.status(500).json({ massage: 'Internal Server Error' })
    }
}

module.exports = {signUp, signIn, signOut, getUserProfile,}
const jwt = require('jsonwebtoken')

// Middleware for JWT verification
const verifyToken = (req, res, next) => {
    const token = req.headers.authorization

    if (!token) {
        return res.status(401).json({ message: "Token not provided" })
    }

    jwt.verify(token, process.env.SECRET_KEY, (err, decoded) => {
        if (err) {
            console.error('Invalid token:', token)
            return res.status(401).json({ message: "Invalid token" })
        }

        // Add decoded token to the request object for use in the next endpoint
        req.user = decoded;
        next()
    })
}

module.exports = { verifyToken }
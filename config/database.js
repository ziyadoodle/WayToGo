// Require mysql
var mysql = require('mysql')
require('dotenv').config()

// Create a connection
var connection = mysql.createConnection({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_DATABASE,
})

// Export the connection
module.exports = connection
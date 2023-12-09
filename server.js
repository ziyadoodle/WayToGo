const express = require('express')
const bodyParser = require('body-parser')
const routes = require('./routes/routes')
require('dotenv').config()

const app = express()

// // Parsing body
app.use(express.urlencoded({ extended: false }))
// Parsing JSON
app.use(express.json())
app.use(bodyParser.json())
// Set port
const port = 3000
// Trust proxy
app.set('trust proxy', true)
// Set view engine
app.set('view engine', 'ejs')

app.use('/', routes)

app.listen(port, () => {
    console.log(`running at http://localhost:${port}`)
})
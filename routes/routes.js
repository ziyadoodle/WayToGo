const express = require('express')
const {signUp, signIn, signOut, getUserProfile} = require('../controller/authCon')
const {place, placeByName, detailPlace, popularPlace, placeByCity} = require('../controller/placeCon')
const { verifyToken } = require('../middleware/auth-middleware')


const route = express.Router()
//=================================================================================================================
route.post('/signup', signUp)
route.post('/signin', signIn)
route.post('/signOut', signOut)

//route user profile
route.get('/profile', verifyToken, getUserProfile)


route.get('/place', verifyToken, place)
route.get('/search/:name', verifyToken, placeByName)
route.get('/search/:formatted_address', verifyToken, placeByCity)
route.get('/place/:place_id', verifyToken, detailPlace)
route.get('/popular', verifyToken, popularPlace)

module.exports = route
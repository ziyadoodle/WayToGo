//firebase admin SDK
require('cross-fetch/polyfill')
const client = require('@firebase/app')
const { getAuth } = require('@firebase/auth')
const admin = require('firebase-admin')
const credentials = require("../serviceAccountKey.json")

const firebaseConfig = {
    apiKey: "AIzaSyBIJ7MQN0v-KMxP40LFORngx0TYWrM92UA",
    authDomain: "way-to-go-project.firebaseapp.com",
    projectId: "way-to-go-project",
    storageBucket: "way-to-go-project.appspot.com",
    messagingSenderId: "956453405465",
    appId: "1:956453405465:web:cd70eeecd87a72aca93b88",
    measurementId: "G-7ZESHQN61F"
  };

const app = client.initializeApp(firebaseConfig)

admin.initializeApp({
    credential: admin.credential.cert(credentials)
})

const auth = getAuth(app)

module.exports = { app, auth }

//firebase admin SDK
const { initializeApp } = require('@firebase/app')
const { getAuth } = require('@firebase/auth')
const admin = require('firebase-admin')
const credentials = require("../serviceAccountKey.json")

const firebaseConfig = {
    apiKey: "AIzaSyCmcqjitIX64-9rw9LvWtKXsaOeeTvIw_I",
    authDomain: "waytogo-project.firebaseapp.com",
    projectId: "waytogo-project",
    storageBucket: "waytogo-project.appspot.com",
    messagingSenderId: "236302172695",
    appId: "1:236302172695:web:be9a29d46b845e6edb9252",
    measurementId: "G-CN52QV1YC0"
}

admin.initializeApp({
    credential: admin.credential.cert(credentials)
})

const app = initializeApp(firebaseConfig)
const auth = getAuth(app)

module.exports = { app, auth }

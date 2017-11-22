var databaseManager = require('mongoose');

var Schema = databaseManager.Schema;

// create a schema
var firebaseSchema = new Schema({
    user_id: Number,
    fb_token: String
});

// the schema is useless so far
// we need to create a model using it
var Firebase = databaseManager.model('Firebase', firebaseSchema);

// make this available to our users in our Node applications
module.exports = Firebase;
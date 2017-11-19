var databaseManager = require('mongoose');

var Schema = databaseManager.Schema;

// create a schema
var chatSchema = new Schema({
    sender_id: Number,
    receiver_id: Number,
    message: String,
    sent_at: Date
});

chatSchema.pre('save', function(next) {
    // get the current date
    this.sent_at = new Date();
    next();
});

// the schema is useless so far
// we need to create a model using it
var Chat = databaseManager.model('Chat', chatSchema);

// make this available to our users in our Node applications
module.exports = Chat;
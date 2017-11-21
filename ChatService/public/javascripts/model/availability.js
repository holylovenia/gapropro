var databaseManager = require('mongoose');

var Schema = databaseManager.Schema;

// create a schema
var availabilitySchema = new Schema({
    user_id: Number,
    online: { type: Number, default: 1 },
    finding_order: { type: Number, default: 0 },
    token: String
});

// the schema is useless so far
// we need to create a model using it
var Availability = databaseManager.model('Availability', availabilitySchema);

// make this available to our users in our Node applications
module.exports = Availability;
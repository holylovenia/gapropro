/**
 * Firebase Manager
 * Manages firebase token and cloud messaging
 * Main route : /firebase
 */

var express = require('express');
var router = express.Router();
var cors = require('cors');
var bodyParser = require('body-parser');
router.use(cors());
router.use(bodyParser());

var databaseManager = require('./model/mongo_manager');
var Firebase = require('./model/firebase');

router.post('/set_token', function (req, res, next) {
    var uId = req.body.userId;
    var fbToken = req.body.firebaseToken;
    Firebase.findOne({user_id: uId}, function (err, user) {
        if (user !== null) {
            user.fb_token = fbToken;
            user.save();
        } else {
            var firebase = new Firebase({
                user_id: uId,
                fb_token: fbToken
            });
            firebase.save();
        }
    });

    res.set('Content-Type', 'application/json');
    res.send('{"status":true}');
});

module.exports = router;
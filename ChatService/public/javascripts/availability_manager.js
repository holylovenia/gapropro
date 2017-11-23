/**
 * Availability Manager
 * Manages availability of a driver
 * Main route : /availability
 */
var express = require('express');
var router = express.Router();
var cors = require('cors');
var bodyParser = require('body-parser');
router.use(cors());
router.use(bodyParser());

var databaseManager = require('./model/mongo_manager');
var Availability = require('./model/availability');

router.post('/add_new_user', function (req, res, next) {
    var uId = req.body.userId;

    var availability = new Availability({
        user_id: uId
    });
    availability.save();

    res.set('Content-Type', 'application/json');
    res.send('{"status":true}');
});

router.post('/set_status', function (req, res, next) {
    var uId = req.body.userId;
    var oStatus = req.body.onlineStatus;
    var fStatus = req.body.findingOrder;

    Availability.findOne({user_id: uId}, function (err, user) {
        if (user !== null) {
            user.online = oStatus;
            user.finding_order = fStatus;
            user.save();
        } else {
            var availability = new Availability({
                user_id: uId,
                online: oStatus,
                finding_order: fStatus
            });
            availability.save();
        }
    });

    res.set('Content-Type', 'application/json');
    res.send('{"status":true}');
});

router.post('/set_online', function (req, res, next) {
    var uId = req.body.userId;
    var status = req.body.onlineStatus;

    Availability.findOne({user_id: uId}, function (err, user) {
        if (user !== null) {
            user.online = status;
            user.save();
        } else {
            var availability = new Availability({
                user_id: uId,
                online: status
            });
            availability.save();
        }
    });

    res.set('Content-Type', 'application/json');
    res.send('{"status":true}');
});

router.post('/set_finding_order', function (req, res, next) {
    var uId = req.body.userId;
    var status = req.body.findingOrder;

    Availability.findOne({user_id: uId}, function (err, user) {
        if (user !== null) {
            user.finding_order = status;
            user.save();
        } else {
            var availability = new Availability({
                user_id: uId,
                finding_order: status
            });
            availability.save();
        }
    });

    res.set('Content-Type', 'application/json');
    res.send('{"status":true}');
});

router.get('/get_available_users', function (req, res, next) {
    Availability.find({$and: [{"online": 1}, {"finding_order": 1}]}, 'user_id', function (err, users) {
        if (err) throw err;

        var returnObj = {};
        returnObj.result = users;

        res.set('Content-Type', 'application/json');
        res.send(JSON.stringify(returnObj));
    });
});

router.get('/choose_driver', function(req, res){
    var sId = parseInt(req.body.senderId);
    var rId = parseInt(req.body.receiverId);
    var m = req.body.chatMessage;
    var token = "";
    Firebase.findOne({'user_id': rId}, function (err, firebase) {
        if (err) throw err;
        token = firebase.fb_token;

        var options = {
            method: 'post',
            json: true,
            url: "https://fcm.googleapis.com/fcm/send",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "key=AIzaSyDLWhhAQ3MmOPvhwXCny7kLw_HyWWq2A6g"
            },
            body: {
                "data": {
                    "notification": {
                        "body": req.body.chatMessage,
                        "title": "New Message"
                    }
                },

                "to": token
            }
        };

        request(options, function (err, res, body) {
            if (err) {
                console.log('Error :', err);
                return true;
            }
        });
    });
});

module.exports = router;

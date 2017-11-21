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
    res.send("Status is changed to online " + oStatus + " and finding order " + fStatus);
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
    res.send("Online is changed to " + status);
});

router.post('/set_finding_order', function (req, res, next) {
    var uId = req.body.userId;
    var status = req.body.findingOrder;
    var fb_token = req.body.fb_token;
    Availability.findOne({user_id: uId}, function (err, user) {
        if (user !== null) {
            user.finding_order = status;
            user.token = fb_token;
            user.save();
        } else {
            var availability = new Availability({
                user_id: uId,
                finding_order: status,
                token: fb_token
            });
            availability.save();
        }
    });
    res.send("Finding order is changed to " + status);
});

router.get('/get_available_users', function(req, res, next) {
    Availability.find({$and:[{"online": 1},{"finding_order": 1}]}, 'user_id', function(err, users) {
        if(err) throw err;
        res.send(users);
    });
});

module.exports = router;

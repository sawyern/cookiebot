var axios = require('axios')
var Discord = require('discord.io');
var auth = require('./auth.json');

const baseurl = 'ec2-18-224-150-136.us-east-2.compute.amazonaws.com';
const pokeApiUrl = 'https://pokeapi.co/api/v2/pokemon/';
const cookieUrl = 'http://localhost:8080/api/cookiebot/v1'

// Configure logger settings


// Initialize Discord Bot
var bot = new Discord.Client({
   token: auth.token,
   autorun: true
});

bot.on('ready', function (evt) {
    console.log('Connected')
    console.log('Logged in as: ')
    console.log(bot.username + ' - (' + bot.id + ')')
});

bot.on('message', function (user, userID, channelID, message, evt) {
    // Our bot needs to know if it will execute a command
    // It will listen for messages that will start with `!`
    if (message.substring(0, 1) == '!') {
        var args = message.substring(1).split(' ')
        var cmd = args[0]

//        if (user === 'eelkire') {
//            bot.sendMessage({
//                to: channelID,
//                message: 'I refuse to service user eelkire'
//            })
//            return
//        }

        args = args.splice(1)
        switch(cmd) {
            // !ping
            case 'ping':
                bot.sendMessage({
                    to: channelID,
                    message: 'Hi ' + user
                })
            break
            case 'register':
                registerAccount(userID)
                .then(() => {
                    bot.sendMessage({
                        to: channelID,
                        message: `Successfully registered ${user}.`
                    })
                }).catch(error => {
                    bot.sendMessage({
                        to: channelID,
                        message: 'Error registering account.'
                    })
                })
            break
            case 'cookies':
                let u
                if (args[0])
                    u = findIdByName(args[0])
                else
                    u = userID

                getCookies(u)
                .then(data => {
                    bot.sendMessage({
                        to: channelID,
                        message: `${bot.users[u].username} has ${data.numCookies} cookies.`
                    })
                }).catch(error => {
                    bot.sendMessage({
                        to: channelID,
                        message: 'Error getting cookies.'
                    })
                })
            break
            case 'givecookie':
                giveCookie(userID, args[0], findIdByName(args[1]))
                .then(() => {
                    bot.sendMessage({
                        to: channelID,
                        message: `Successfully transferred ${args[0]} cookies.`
                    })
                }).catch(error => {
                    console.log(error.data.message)
                    bot.sendMessage({
                        to: channelID,
                        message: error.data.message
                    })
                })
                break
            case 'help':
                bot.sendMessage({
                    to: channelID,
                    message: getCommandList()
                })
            break
            default:
                bot.sendMessage({
                    to: channelID,
                    message: 'Invalid command ' + cmd + '. Try !help for more information.'
                })
         }
     }
});

function getCommandList () {
    let returnString = '```'
    let commands = []
    commands.push('Register Account -> !register')
    commands.push('My Cookies -> !cookies')
    commands.push('Their Cookies -> !cookies {username}')
    commands.push('Give Cookies -> !givecookie {numCookies} {username}')
    commands.forEach(command => {
        returnString += command + '\n'
    })
    returnString += '```'
    return returnString
}

function findIdByName(name) {
    try {
    let users = bot.users
    for (let key in users) {
        let user = users[key]
        if (user.username.toUpperCase() === name.toUpperCase()) {
            return user.id
        }
    }
    return null
    } catch {
        return null
    }
}

function giveCookieRequest (userID, numCookies, recipientId) {
    console.log('making request to: ' + cookieUrl + `/cookies/give?id=${userID}?num=${numCookies}?recipientId=${recipientId}`)
    return new Promise((resolve, reject) => {
        axios.post(cookieUrl + `/cookies/give?id=${userID}&num=${numCookies}&recipientId=${recipientId}`)
        .then(response => {
            console.log('success')
            resolve(response.data)
        }).catch (error => {
            console.log('failed')
            reject(error)
        })
    })
}

async function giveCookie (userID, numCookies, recipientId) {
    return await giveCookieRequest (userID, numCookies, recipientId)
}

function register (userID) {
    console.log('making request to: ' + cookieUrl + `/accounts?id=${userID}`)
    return new Promise((resolve, reject) => {
        axios.put(cookieUrl + `/accounts?id=${userID}`)
        .then(response => {
            console.log('success')
            resolve(response.data)
        }).catch (error => {
            console.log('failed')
            reject(error)
        })
    })
}

async function registerAccount (userID) {
    return await register (userID)
}

function getCookies (userID) {
    console.log('making request to: ' + cookieUrl + `/cookies?id={userID}`)
    return new Promise((resolve, reject) => {
        axios.get(cookieUrl + `/cookies?id=${userID}`)
        .then(response => {
            console.log('success')
            resolve(response.data)
        }).catch (error => {
            console.log('failed')
            reject(error)
        })
    })
}

function getPokemonData (name) {
    console.log('making request to: ' + pokeApiUrl + name)
    return new Promise((resolve, reject) => {
        axios.get(pokeApiUrl + name)
        .then(response => {
            console.log('success')
            resolve(response.data)
        }).catch(error => {
            console.log('failed')
            reject(error)
         })
     })
}

async function getPokemonHeight (name) {
    return await getPokemonData(name)
    .then(data => {
        console.log(data.height)
        return data.height
    })
}

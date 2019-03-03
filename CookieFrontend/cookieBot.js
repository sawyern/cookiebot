var axios = require('axios')
var Discord = require('discord.io');
var auth = require('./auth.json');

const baseurl = 'ec2-18-224-150-136.us-east-2.compute.amazonaws.com';
const pokeApiUrl = 'https://pokeapi.co/api/v2/pokemon/';
const animeApiUrl = 'https://hummingbirdv1.p.rapidapi.com/anime/'

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
       
        args = args.splice(1)
        switch(cmd) {
            // !ping
            case 'ping':
                bot.sendMessage({
                    to: channelID,
                    message: 'fuck you'
                })
            break
            case 'howTall':
                getPokemonHeight(args[0])
                .then(data => {
                    bot.sendMessage({
                        to: channelID,
                        message: args[0] + ' is ' + (data / 10) + 'm tall'
                    })
                })
            break
            case 'animeInfo':
            getAnimeVal(args[0], args[1])
            .then(data => {
                bot.sendMessage({
                    to: channelID,
                    message: data
                })
            })
            break
         }
     }
});

function getPokemonData (name) {
    console.log('making request to: ' + pokeApiUrl + name)
    return new Promise((resolve, reject) => {
        axios.get(pokeApiUrl + name)
        .then(response => {
            console.log('success')
            resolve(response.data)
        })
    }, error => {
        reject(error.error)
    })
}

async function getPokemonHeight (name) {
    return await getPokemonData(name)
    .then(data => {
        console.log(data.height)
        return data.height
    }).catch(error => {
        return error.error
    })
}

function getAnimeData (name) {
    console.log('making request to: ' + animeApiUrl + name)
    return new Promise((resolve, reject) => {
        axios.get(animeApiUrl + name, { 'headers': { 'X-RapidAPI-Key': '5f9fb1ac95msh462e2fffa90613dp116d80jsncc3406f6fe20'}})
        .then(response => {
            console.log('success')
            resolve(response.data)
        })
    }, error => {
        reject(error.error)
    })
}

async function getAnimeVal(name, arg) {
    return await getAnimeData(name)
    .then(data => {
        console.log(data)
        return data
    }).catch(error => {
        throw error.error
    })
}
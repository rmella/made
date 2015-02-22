/*    
 @licstart  The following is the entire license notice for the 
 JavaScript code in this page.
 
 Copyright (C) 2014  Rubén Héctor García Ortega
 
 The JavaScript code in this page is free software: you can
 redistribute it and/or modify it under the terms of the GNU
 General Public License (GNU GPL) as published by the Free Software
 Foundation, either version 3 of the License, or (at your option)
 any later version.  The code is distributed WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU GPL for more details.
 
 As additional permission under GNU GPL version 3 section 7, you
 may distribute non-source (e.g., minimized or compacted) forms of
 that code without the copy of the GNU GPL normally required by
 section 4, provided you include this license notice and a URL
 through which recipients can access the Corresponding Source.   
 
 
 @licend  The above is the entire license notice
 for the JavaScript code in this page.
 */

/**
 * MADE JS - A javascript implementation of MADE
 * @author R.H. García-Ortega (rhgarcia@fidesol.org)
 * @type @exp;MADEJS
 */
var MADEJS = MADEJS || {};

/**
 * A MADE environment. Generates the world's backstories by using a M.A.S.
 * 
 * @param Configuration configuration the map configuration
 * @param Chromosome chromosome the agent chromosome
 * @returns {MADEJS.Environment.Anonym$0}
 */
MADEJS.Environment = function(configuration, chromosome) {
    this.configuration = configuration;
    this.chromosome = chromosome;
    var agents = [];
    
    /**
     * executes the environment
     */
    function run() {
        for (var i = 0; i < configuration.mapDays; i++) {
            var agent = new MADEJS.Agent(i, 0, 0);
            agents[i] = agent;
        }
    }
    
    /**
     * returns the agent with the given id
     * @param {integer} id the id of the agent
     * @return {Agent} the agent with the given id
     */
    function getAgent(id) {
        return agents[id];
    }
    return{
        configuration: configuration,
        chromosome: chromosome,
        agents: agents,
        run: run,
        getAgent: getAgent
    }
};

/**
 * Environment configuration
 * @returns {Configuration}
 */
MADEJS.Configuration = function() {
    this.mapDimension = 10; // map dimension
    this.mapFood = 10; // food per day
    this.mapAgents = 6; // food per day
    this.mapVisibility = 50; // % of the visibility in the map
    this.mapDays = 50; // maximum number of days
};

/**
 * A made Agent
 * 
 * @param {integer} id the unique identifier for an Agent
 * @param {integer} father the agent's father's id
 * @param {integer} mother the agent's mother's id
 * @param {Chromosome} chromosome agent Personality
 * @returns {undefined}
 */
MADEJS.Agent = function(id, father, mother, chromosome) {
    this.id = id;
    this.name =
            MADEJS.NameHelper.prototype.instance.getName("male") + " " +
            MADEJS.NameHelper.prototype.instance.getName("surname");
    this.father = father;
    this.mother = mother;
    this.birth = 0;
    this.death = 0;
    this.mate = 0;
    this.children = 0;
    this.archetypes = 0;
    this.chromosome = chromosome;
    this.state = '';
};

MADEJS.Agent.prototype.STATES = {
    health: {
        HUNGRY: "HUNGRY",
        WEAK: "WEAK",
        HEALTHY: "HEALTHY",
        DEAD: "DEAD"
    },
    love: {
        SINGLE: "SINGLE",
        INTERESTED: "INTEREST",
        MATCHED: "MATCHED"
    },
    pregnancy: {
        NOT_PREGNANT: "NOT_PREGNANT",
        PREGNANT: "PREGNANT"
    }
}
MADEJS.Agent.prototype.ACTIONS = {
    IS_BORN: "IS_BORN",
    IS_HUNGRY: "",
    IS_HEALTHY: "",
    IS_DEAD: "",
    EATS: "",
    GRANTS: "",
    ATTACKS: "",
    DEFENDS: "",
    MOVES: "",
    IS_DISPLACED: "",
    IS_SINGLE: "",
    IS_INTERESTED_IN: "",
    IS_MATCHED_WITH: "",
    IS_PREGNANT: "",
    IS_NOT_PREGNANT: "",
    FREE_TIME: ""
}



MADEJS.NameHelper = function() {
    this.listeners = [];
    function ready(f) {
        if (MADEJS.NameHelper.prototype.names == null) {
            MADEJS.NameHelper.prototype.listeners.push(f);
            console.log("queuing");
        } else {
            f();
        }
    }
    function callListeners() {
        debugger;
        var l = MADEJS.NameHelper.prototype.listeners.length;
        for (var i = 0; i < l; i++) {
            MADEJS.NameHelper.prototype.listeners[i]();
            console.log("calling");
        }
        MADEJS.NameHelper.prototype.listeners = [];
    }
    function getName(theClass) {
        if (theClass === "male") {
            var l = MADEJS.NameHelper.prototype.names.male.length;
            return MADEJS.NameHelper.prototype.names.male[MADEJS.Utils.getRandomInt(0, l)];
        } else if (theClass === "female") {
            var l = MADEJS.NameHelper.prototype.names.female.length;
            return MADEJS.NameHelper.prototype.names.female[MADEJS.Utils.getRandomInt(0, l)];
        } else if (theClass === "surname") {
            var l = MADEJS.NameHelper.prototype.names.surname.length;
            return MADEJS.NameHelper.prototype.names.surname[MADEJS.Utils.getRandomInt(0, l)];
        }
    }
    return{
        ready: ready,
        callListeners: callListeners,
        listeners: this.listeners,
        getName: getName
    }
}

MADEJS.NameHelper.prototype.names = null;
MADEJS.NameHelper.prototype.listeners = [];
MADEJS.NameHelper.prototype.instance = new MADEJS.NameHelper();
$.ajax({
    dataType: "json",
    url: "js/json/names.json",
    success: function(data) {
        MADEJS.NameHelper.prototype.names = data;
        new MADEJS.NameHelper().callListeners();
    }
});



// from the examples: 
// https://developer.mozilla.org/en-US/docs/Web/
// JavaScript/Reference/Global_Objects/Math/random
MADEJS.Utils = {
    // Returns a random number between 0 (inclusive) and 1 (exclusive)
    getRandom: function() {
        return Math.random();
    },
    // Returns a random number between min (inclusive) and max (exclusive)
    getRandomArbitrary: function(min, max) {
        return Math.random() * (max - min) + min;
    },
    // Returns a random integer between min (included) and max (excluded)
    // Using Math.round() will give you a non-uniform distribution!
    getRandomInt: function(min, max) {
        return Math.floor(Math.random() * (max - min)) + min;
    },
    // checks if MADEJS is ready (as loaded all the external json files)
    ready: function(f) {
        var nh = MADEJS.NameHelper.prototype.instance;
        nh.ready(f);
    }
}
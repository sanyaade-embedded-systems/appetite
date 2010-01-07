function Appetite(apps) {
    this._apps = {
        all: apps,
        free: [],
        paid: []
    };

    this._icons = [];
    this._byid = {};

    var self = this;

    var iconInfo = function(app) {
        return {
          id: app.guid,
          icon: app.icons[0].url,
          name: app.title
        }
    };

    var generateCaches = function() {
        for (var i in apps) {
            if (apps.hasOwnProperty(i)) {
                var app = apps[i];
                self._icons.push(iconInfo(app));
                self._byid[app.guid] = app;

                // dip into the first localization
                if (app.localizations[0]) {
                    var amount = app.localizations[0].price;
                    if (amount == 0) {
                        self._apps['free'].push(app);
                    } else {
                        self._apps['paid'].push(app);
                    }
                }
            }
        }
    };

    generateCaches();
}

Appetite.prototype = {
    /* the engine behind the whole thing */
    find: function(opts) {
        // get the options ready
        opts = this.withDefaults(opts);

        // get the initial data set ready
        var apps = this._apps[opts.set];

        // filter the data set if there is a query
        if (opts.query) {
            apps = this.filter(apps, opts.query, opts.channels);
        // else just weed the channels if we need too
        } else if (opts.channels != 'bcw') {
            apps = this.weedChannels(apps, opts.channels);
        }

        // sort
        apps = apps.sort(this.sorts[opts.order]);

        // return a subset back
        return apps.slice(opts.start, opts.size);
    },
    
    appIsInChannel: function(channels, appchannel) {
        appchannel = appchannel || 'c';
        
        return (channels.indexOf(appchannel) > -1); // if this app is in the channels that we want
    },
    
    weedChannels: function(apps, channels) {
        var results = [];

        for (var i in apps) {
            if (apps.hasOwnProperty(i)) {
                var app = apps[i];

                // check channel and stop it if not in the channel!
                // NOTE: if there is no channel, assume that it is device
                if (this.appIsInChannel(channels, app.channel)) {
                    results.push(app);                    
                }
            }
        }
        return results;        
    },

    filter: function(apps, query, channels) {
        var results = [];
        query = query.toLowerCase();

        for (var i in apps) {
            if (apps.hasOwnProperty(i)) {
                var app = apps[i];

                // check channel and stop it if not in the channel!
                // NOTE: if there is no channel, assume that it is device
                if (this.appIsInChannel(channels, app.channel)) {
                    if ( (app.title.toLowerCase().indexOf(query) > -1) || (app.description.toLowerCase().indexOf(query) > -1) ) {
                        results.push(app);
                    }
                }
            }
        }
        return results;
    },

    withDefaults: function(opts) {
        opts = opts || {};

        // Get the right settings for a given type of query
        if (opts.type && this.types[opts.type]) {
            opts = this.types[opts.type](opts);
        }

        if (! (opts.set == 'free' || opts.set == 'paid' || opts.set == 'all' ) ) opts.set = 'all';
        if (! (opts.order == 'alpha' || opts.order == 'rating' || opts.order == 'downloads' || opts.order == 'gross' || opts.order == 'newest') ) opts.order = 'alpha';
        opts.start    = opts.start-1 || 0; // 1 based? really? :)
        opts.size     = opts.size || 50;
        opts.channels = opts.channels || 'bcw'; // b: beta, c: palm catalog, w: web distro

        return opts;
    },

    // -- From Type
    types: {
        top_rated: function(opts) {
            opts.order = 'rating';
            return opts;
        },
        top_paid: function(opts) {
            opts.order = 'downloads';
            opts.set = 'paid';
            return opts;
        },
        top_free: function(opts) {
            opts.order = 'downloads';
            opts.set = 'free';
            return opts;
        },
        top_overall: function(opts) {
            opts.order = 'downloads';
            return opts;
        },
        top_grossing: function(opts) {
            opts.order = 'gross';
            return opts;
        },
        newest: function(opts) {
            opts.order = 'newest';
            return opts;
        }
    },

    // -- Sorts
    sorts: {
        alpha: function(a, b) {
            if (a.title == b.title) return 0;

            return (a.title > b.title) ? 1 : -1;
        },

        rating: function(a, b) {
            var ratingA = parseFloat(a.rating);
            var ratingB = parseFloat(b.rating);

            if (ratingA == ratingB) return 0;

            return (ratingA < ratingB) ? 1 : -1;
        },

        downloads: function(a, b) {
            var downloadsA = parseInt(a.total_downloads);
            var downloadsB = parseInt(b.total_downloads);

            if (downloadsA == downloadsB) return 0;

            return (downloadsA < downloadsB) ? 1 : -1;
        },

        gross: function(a, b) {
            var downloadsA = parseInt(a.total_downloads);
            var downloadsB = parseInt(b.total_downloads);

            var priceA = parseFloat(a.localizations[0].price);
            var priceB = parseFloat(b.localizations[0].price);

            var grossA = downloadsA * priceA;
            var grossB = downloadsB * priceB;

            if (grossA == grossB) return 0;

            return (grossA < grossB) ? 1 : -1;
        },

        newest: function(a, b) {
            if (a.pubDate == b.pubDate) return 0;

            return (a.pubDate < b.pubDate) ? 1 : -1;
        }
    },

    // -- API

    // /icons?count=[1+]&uniqueIcons=[true|false]
    icons: function(count, uniqueIcons) {
        count = count || 100;
        if (count > this._icons.length) {
            count = this._icons.length;
        }
        uniqueIcons = uniqueIcons || true; // TODO ignore for now

        return this._icons.slice(0, count);
    },

    // /app?id=[id]&locale=*
    // TODO: Ignore locale
    app: function(id, locale) {
        return this._byid[id];
    }
};

// check to see if you are running inside of node.js and export if you are
if (typeof GLOBAL == "object" && typeof GLOBAL['node'] == "object") {
    exports.Appetite = Appetite;
}
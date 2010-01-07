function Appetite(data) {
    this._data = data;
    this._items = {
        all: data.channel.items,
        free: [],
        paid: []
    };

    this._icons = [];
    this._byid = {};

    var self = this;

    var iconInfo = function(item) {
        return {
          id: item.guid,
          icon: item.icons[0].url,
          name: item.title
        }
    };

    var generateCaches = function() {
        var items = data.channel.items;

        for (var i in items) {
            if (items.hasOwnProperty(i)) {
                var item = items[i];
                self._icons.push(iconInfo(item));
                self._byid[item.guid] = item;

                // dip into the first localization
                if (item.localizations[0]) {
                    var amount = item.localizations[0].price;
                    if (amount == 0) {
                        self._items['free'].push(item);
                    } else {
                        self._items['paid'].push(item);
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
        var data = this._items[opts.data];

        // filter the data set if there is a query
        if (opts.query) {
            data = this.filter(data, opts.query, opts.channel);
        }

        // sort
        data = data.sort(this.sorts[opts.order]);

        // return a subset back
        return data.slice(opts.start, opts.size);
    },

    filter: function(data, query, channel) {
        var results = [];
        query = query.toLowerCase();

        for (var i in data) {
            if (data.hasOwnProperty(i)) {
                var item = data[i];

                // check channel and stop it if not in the channel!
                // TODO

                if ( (item.title.toLowerCase().indexOf(query) > -1) || (item.description.toLowerCase().indexOf(query) > -1) ) {
                    results.push(item);
                }
            }
        }
        return results;
    },

    withDefaults: function(opts) {
        opts = opts || {};
        // if (opts.byCategory) {
        //     // deal with category sorting
        // }

        // Get the right settings for a given type of query
        if (opts.type && this.types[opts.type]) {
            opts = this.types[opts.type](opts);
        }

        if (! (opts.data == 'free' || opts.data == 'paid' || opts.data == 'all' ) ) opts.data = 'all';
        if (! (opts.order == 'alpha' || opts.order == 'rating' || opts.order == 'downloads' || opts.order == 'gross' || opts.order == 'newest') ) opts.order = 'alpha';
        opts.start   = opts.start-1 || 0; // 1 based? really? :)
        opts.size    = opts.size || 50;
        opts.channel = opts.channel || 'wcb';

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
            opts.data = 'paid';
            return opts;
        },
        top_free: function(opts) {
            opts.order = 'downloads';
            opts.data = 'free';
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
    },

    // -- EXPLORE (LEGACY)

    // /explore/top_rated?by_category=[true|false]&start=[1+]&size=[1+]&channel=[w][b][c]
    explore_top_rated: function(opts) {
        opts = opts || {};
        opts.type = 'top_rated';

        return this.find(opts);
    },

    // /explore/top_paid
    explore_top_paid: function(opts) {
        opts = opts || {};
        opts.type = 'top_paid';

        return this.find(opts);
    },

    explore_top_free: function(opts) {
        opts = opts || {};
        opts.type = 'top_free';

        return this.find(opts);
    },

    explore_top_overall: function(opts) {
        opts = opts || {};
        opts.type = 'top_overall';

        return this.find(opts);
    },

    explore_top_grossing: function(opts) {
        opts = opts || {};
        opts.type = 'top_grossing';

        return this.find(opts);
    },

    explore_newest: function(opts) {
        opts = opts || {};
        opts.type = 'newest';

        return this.find(opts);
    },

    explore_all: function(opts) { // all really means "by alpha" and this will go when I can tweak the browser front end code
        return this.find(opts);
    }

};

// check to see if you are running inside of node.js and export if you are
if (typeof GLOBAL == "object" && typeof GLOBAL['node'] == "object") {
    exports.Appetite = Appetite;
}
function Appetite(data) {
    this._data = data;
    this._icons = [];
    this._byid = {};
    this._free = [];
    this._paid = [];

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
                    var amount = item.localizations[0].price.amount;
                    if (amount == 0) {
                        self._free.push(item);
                    } else {
                        self._paid.push(item);
                    }                    
                }
            }
        }        
    };

    generateCaches();
}

Appetite.prototype = {
    data: function() {
        return this._data;
    },
    
    withDefaults: function(opts) {
        opts = opts || {};
        if (opts.byCategory) {
            // deal with category sorting
        }

        opts.start   = opts.start-1 || 0; // 1 based? really? :)
        opts.size    = opts.size || 50;
        opts.channel = opts.channel || 'wcb';  
        
        return opts;
    },
    
    filter: function(filterFunc, opts) {
        return filterFunc(withDefaults(opts));
    },
    
    // -- Sorts
    sortByRating: function(a, b) {
        var ratingA = parseFloat(a.rating);
        var ratingB = parseFloat(b.rating);
        
        if (ratingA == ratingB) return 0;
        
        return (ratingA < ratingB) ? 1 : -1;
    },
    
    sortByDownloads: function(a, b) {
        var downloadsA = parseInt(a.total_downloads);
        var downloadsB = parseInt(b.total_downloads);
        
        if (downloadsA == downloadsB) return 0;
        
        return (downloadsA < downloadsB) ? 1 : -1;
    },
    
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
    app: function(id, locale) {
        return this._byid[id];
    },
    
    // -- EXPLORE
    
    // /explore/top_rated?by_category=[true|false]&start=[1+]&size=[1+]&channel=[w][b][c]
    explore_top_rated: function(opts) {
        opts = this.withDefaults(opts);
        
        return this.data().channel.items.sort(this.sortByRating).slice(opts.start, opts.size);
    },
    
    // /explore/top_paid
    explore_top_paid: function(opts) {
        opts = this.withDefaults(opts);
        
        return this._paid.sort(this.sortByDownloads).slice(opts.start, opts.size);
    },

    explore_top_free: function(opts) {
        opts = this.withDefaults(opts);
        
        return this._free.sort(this.sortByDownloads).slice(opts.start, opts.size);
    },

    explore_all: function(opts) {
        opts = this.withDefaults(opts);

        return this.data().channel.items.slice(opts.start, opts.size);
    },

    explore_newest: function(opts) {
        opts = this.withDefaults(opts);
        
        var byNewestDate = function(a, b) {
            if (a.pubDate == b.pubDate) return 0;
            
            return (a.pubDate < b.pubDate) ? 1 : -1;
        }

        return this.data().channel.items.sort(byNewestDate).slice(opts.start, opts.size);
    },
    
    // -- SEARCH
    search_all: function(query, opts) {
        opts = this.withDefaults(opts);
        
        var items = this.data().channel.items;
        var results = [];
        query = query.toLowerCase();
        
        for (var i in items) {
            if (items.hasOwnProperty(i)) {
                var item = items[i];
                if (item.title.toLowerCase().indexOf(query) > -1) {
                    results.push(item);
                }
            }
        }
        return results.slice(opts.start, opts.size);
    }
    
};
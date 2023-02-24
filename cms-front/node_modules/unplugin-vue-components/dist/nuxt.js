"use strict";Object.defineProperty(exports, "__esModule", {value: true});

var _chunkL42GFWTAjs = require('./chunk-L42GFWTA.js');
require('./chunk-44ONIEX7.js');
require('./chunk-BTQOTIPQ.js');
require('./chunk-6F4PWJZI.js');

// src/nuxt.ts
function nuxt_default(options) {
  this.extendBuild((config) => {
    config.plugins = config.plugins || [];
    config.plugins.unshift(_chunkL42GFWTAjs.unplugin_default.webpack(options));
  });
  this.nuxt.hook("vite:extend", async (vite) => {
    vite.config.plugins = vite.config.plugins || [];
    vite.config.plugins.push(_chunkL42GFWTAjs.unplugin_default.vite(options));
  });
}


module.exports = nuxt_default;
exports.default = module.exports;
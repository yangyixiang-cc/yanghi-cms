"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = isValidVariable;

/**
 * Check whether the atrule is valid less variable.
 *
 * @param {atRule} node
 *
 * @returns {true or false}
 */
function isValidVariable(atRule) {
  if ('variable' in atRule && atRule.raws.afterName.includes(":") || atRule.mixin) {
    return true;
  }

  return false;
}
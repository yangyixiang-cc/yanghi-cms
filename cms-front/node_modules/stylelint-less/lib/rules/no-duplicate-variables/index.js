"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = _default;
exports.ruleName = exports.messages = void 0;

var _utils = require("../../utils");

var stylelint = require("stylelint");

var isStandardSyntaxAtRule = require('stylelint/lib/utils/isStandardSyntaxAtRule');

var validateOptions = require('stylelint/lib/utils/validateOptions');

var ruleName = (0, _utils.namespace)("no-duplicate-variables");
exports.ruleName = ruleName;
var messages = stylelint.utils.ruleMessages(ruleName, {
  rejected: function rejected(prop) {
    return "unexpected duplicate property in \"".concat(prop, "\"");
  },
  invalid: function invalid(variableName) {
    return "Unexpected Invalid variable  \"".concat(variableName, "\"");
  }
});
exports.messages = messages;

function _default(actual, options) {
  return function (root, result) {
    var validOptions = validateOptions(result, ruleName, {
      actual: actual
    });

    if (!validOptions) {
      return;
    }

    var globalVariables = [];
    root.walkRules(function (rule) {
      var variables = [];
      rule.nodes.forEach(function (node) {
        if (node.type == "atrule") {
          if (!isStandardSyntaxAtRule(node)) {
            if (!(0, _utils.isValidVariable)(node)) {
              stylelint.utils.report({
                result: result,
                ruleName: ruleName,
                message: messages.invalid(node.name),
                node: node,
                word: node.name
              });
            } else {
              if (variables.includes(node.name)) {
                stylelint.utils.report({
                  result: result,
                  ruleName: ruleName,
                  message: messages.rejected(node.name),
                  node: node,
                  word: node.name
                });
              } else {
                variables.push(node.name);
              }
            }
          }
        }
      });
    });
    root.walkAtRules(function (node) {
      //check duplicate in globalvariables 
      if (node.parent.type == "root") {
        if (!isStandardSyntaxAtRule(node)) {
          if (!(0, _utils.isValidVariable)(node)) {
            stylelint.utils.report({
              result: result,
              ruleName: ruleName,
              message: messages.invalid(node.name),
              node: node,
              word: node.name
            });
          } else {
            if (globalVariables.includes(node.name)) {
              stylelint.utils.report({
                result: result,
                ruleName: ruleName,
                message: messages.rejected(node.name),
                node: node,
                word: node.name
              });
            } else {
              globalVariables.push(node.name);
            }
          }
        }
      }
    });
  };
}
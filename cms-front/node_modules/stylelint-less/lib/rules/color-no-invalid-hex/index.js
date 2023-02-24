"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = _default;
exports.ruleName = exports.messages = void 0;

var _utils = require("../../utils");

var stylelint = require("stylelint");

var isStandardSyntaxAtRule = require('stylelint/lib/utils/isStandardSyntaxAtRule');

var valueParser = require('postcss-value-parser');

var isValidHex = require('stylelint/lib/utils/isValidHex');

var validateOptions = require('stylelint/lib/utils/validateOptions');

var ruleName = (0, _utils.namespace)("color-no-invalid-hex");
exports.ruleName = ruleName;
var messages = stylelint.utils.ruleMessages(ruleName, {
  rejected: function rejected(value) {
    return "Unexpected invalid hex color \"".concat(value, "\"");
  },
  invalid: function invalid(variableName) {
    return "invalid variable \"".concat(variableName, "\"");
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

    root.walkAtRules(function (node) {
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
          valueParser(node.params).walk(function (_ref) {
            var value = _ref.value,
                type = _ref.type,
                sourceIndex = _ref.sourceIndex;
            if (type === 'function' && value.endsWith('url')) return false;
            if (type !== 'word') return;
            var hexMatch = /^#[0-9A-Za-z]+/.exec(value);
            if (!hexMatch) return;
            var hexValue = hexMatch[0];
            if (isValidHex(hexValue)) return;
            stylelint.utils.report({
              message: messages.rejected(hexValue),
              node: node,
              word: node.params,
              result: result,
              ruleName: ruleName
            });
          });
        }
      }
    });
  };
}

;
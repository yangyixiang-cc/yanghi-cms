"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = _default;
exports.ruleName = exports.messages = void 0;

var _utils = require("../../utils");

var _postcssValueParser = _interopRequireDefault(require("postcss-value-parser"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

var stylelint = require("stylelint");

var isStandardSyntaxAtRule = require('stylelint/lib/utils/isStandardSyntaxAtRule');

var validateOptions = require('stylelint/lib/utils/validateOptions');

var ruleName = (0, _utils.namespace)("color-no-hex");
exports.ruleName = ruleName;
var messages = stylelint.utils.ruleMessages(ruleName, {
  rejected: function rejected(hex) {
    return "Unexpected hex color \"".concat(hex, "\"");
  },
  invalid: function invalid(variableName) {
    return "invalid variable \"".concat(variableName, "\"");
  }
});
exports.messages = messages;
var HEX = /^#[0-9A-Za-z]+/;
var IGNORED_FUNCTIONS = new Set(['url']);

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
          var parsedValue = (0, _postcssValueParser["default"])(node.params);
          parsedValue.walk(function (parsedValueNode) {
            if (isIgnoredFunction(parsedValueNode)) return false;
            if (!isHexColor(parsedValueNode)) return;
            stylelint.utils.report({
              message: messages.rejected(parsedValueNode.value),
              node: node,
              word: parsedValueNode.value,
              result: result,
              ruleName: ruleName
            });
          });
        }
      }
    });
  };
}

function isIgnoredFunction(_ref) {
  var type = _ref.type,
      value = _ref.value;
  return type === 'function' && IGNORED_FUNCTIONS.has(value.toLowerCase());
}

function isHexColor(_ref2) {
  var type = _ref2.type,
      value = _ref2.value;
  return type === 'word' && HEX.test(value);
}
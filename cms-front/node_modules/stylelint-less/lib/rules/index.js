"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;

var _colorNoHex = _interopRequireDefault(require("./color-no-hex"));

var _colorNoInvalidHex = _interopRequireDefault(require("./color-no-invalid-hex"));

var _colorHexCase = _interopRequireDefault(require("./color-hex-case"));

var _noDuplicateVariables = _interopRequireDefault(require("./no-duplicate-variables"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

var _default = {
  "color-no-hex": _colorNoHex["default"],
  "color-no-invalid-hex": _colorNoInvalidHex["default"],
  "no-duplicate-variables": _noDuplicateVariables["default"],
  "color-hex-case": _colorHexCase["default"]
};
exports["default"] = _default;
require('es5-shim/es5-shim');
require('es5-shim/es5-sham');

var React = require('react/addons');
var Router = require("react-router");
var app_router = require('./app/app_router');

Router.run(app_router, function (Handler) {
	React.render(<Handler/>, document.body);
});

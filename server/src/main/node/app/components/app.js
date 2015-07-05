/** @jsx React.DOM */

var React = require("react");
var Router = require("react-router");
var RouteHandler = Router.RouteHandler;

var MainHeader = require('./main_header');

var App = React.createClass({

	render: function () {
		return (
			<div className='app'>
				<MainHeader/>
				<div className='main-content container'>
					<RouteHandler/>
				</div>
			</div>
		);
	}
});

module.exports = App;

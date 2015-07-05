/** @jsx React.DOM */

var React = require("react");

var Router = require("react-router");
var Routes = Router.Routes;
var Route = Router.Route;
var DefaultRoute  = Router.DefaultRoute;
var NotFoundRoute = Router.NotFoundRoute;

// Handlers
var App = require('./components/app');
var ListEntry = require('./components/list_entry');
var NotFoundHandler = require('./components/not_found');

var appRouter = (
	<Route name="list" path="/" handler={App}>
		<DefaultRoute handler={ListEntry} />
		<NotFoundRoute handler={NotFoundHandler}/>
	</Route>
);

module.exports = appRouter;

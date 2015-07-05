/** @jsx React.DOM */

var React = require("react");
var Link = require('react-router').Link;

var MainHeader = React.createClass({

	render: function () {
		return (
			<header className='navbar navbar-default navbar-static-top'>
				<div className='container'>
					<div className="navbar-header">
						<Link to="list" className='navbar-brand logo logoStyle'> The  Daily  Life</Link>
					</div>
				</div>
			</header>
		);
	}
});

module.exports = MainHeader;

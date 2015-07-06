/** @jsx React.DOM */

var React = require("react");

var MainHeader = React.createClass({

	render: function () {
		return (
			<header className='navbar navbar-inverse navbar-static-top'>
				<div className='container'>
					<div className="navbar-header">
						<div className='navbar-brand logoStyle'> The  Daily  Life</div>
					</div>
				</div>
			</header>
		);
	}
});

module.exports = MainHeader;

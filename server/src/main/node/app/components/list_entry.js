/** @jsx React.DOM */

var React = require("react");

var ListEntry = React.createClass({

	render: function () {
		return (
			<div className="media">
				<div className="media-left media-middle">
					<a href="#">
						<img className="media-object" />
					</a>
				</div>
				<div className="media-body">
					<h4 className="media-heading">2015年7月5日</h4>
					<p>空が青い。空気が澄んでいる。</p>
				</div>
			</div>
		);
	}
});

module.exports = ListEntry;

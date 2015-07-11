/** @jsx React.DOM */

var React = require("react");
var service = require('./../infra/service');

var ListEntry = React.createClass({


	getInitialState: function() {
		var initData = [
			{postDate:'', content:''}
		];
		return {
			data : initData
		};
	},
	componentWillMount: function() {
		this._onReload();
	},

	// データ取得
	_onReload : function() {
		var self = this;
		service.api("/user/api/v1/entry").get({},"json").done(function(data) {
			self.setState({data:data});
		});
	},

	render: function () {
		var entries = this.state.data.map(function (entry) {
			return (
				<div className="entry">
					<h4 className="text-center">{entry.postDate}</h4>
					<hr/>
					<p>{entry.content}</p>
				</div>
			);
		});
		return (
			<div className="row">
				<div className="col-md-6 col-md-offset-3">
					{entries}
				</div>
			</div>
		);
	}
});

module.exports = ListEntry;

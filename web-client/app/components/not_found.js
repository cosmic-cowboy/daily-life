/** @jsx React.DOM */
var React = require('react');

var NotFound = React.createClass({
  render:function(){
    return (
	    <div>
	      <h1>404</h1>
	      <p>ページが存在しません</p>
	    </div>
    );
  }
});

module.exports = NotFound;
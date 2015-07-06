var jQuery = require("jquery");


var TIMEOUT_MESSAGE = {
	messageList: [{ message: "接続が中断されました。" }]
};

var ServiceAPI = function (path) {
	this.url = _toUrl(path);
};

ServiceAPI.prototype.get = function(params, type) {
	type = type || 'json';

	return jQuery.ajax({
		type: 'GET',
		url: this.url,
		data: params,
		dataType: type
	});
};

ServiceAPI.prototype.post = function(params, type) {
	type = type || 'json';

	var def = jQuery.Deferred();
	jQuery.ajax({
		type: 'POST',
		url: this.url,
		data: params,
		dataType: type,
		success: function(value) {
			def.resolve(value);
		},
		error: function(xhr) {
			if (xhr.statusText === 'timeout') {
				def.reject(TIMEOUT_MESSAGE);
			} else if (xhr.status === 400) {
				def.reject(JSON.parse(xhr.responseText));
			} else {
				def.reject(xhr.responseText);
			}
		}
	});
	return def.promise();
};

var _toUrl = function(path) {
	var domain = window.location.origin;
	return domain + path;
};

module.exports.api = function(path){
	return new ServiceAPI(path);
};

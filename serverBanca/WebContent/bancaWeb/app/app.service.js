/**
 * Services RESTFUL of the APP
 */
var bancaWebServices= angular.module('bancaWebServices',[ 'ngResource' ]);

bancaWebServices.factory('bancaWebSV', [ '$resource', 
	function($resource) {
		return $resource('/serverBanca/:action', {}, {
				login : {method : 'POST', isArray : false, params:{action: 'login'}, headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'}},
				update: { method:'PUT' },
				save: {method: 'POST'},
				drop: {method:'DELETE'}
			});
		}
]);
bancaWebServices.service('UserCM',function()
		{
			var user = {};
			var getData;
		        return {
		            get: function () {
		                return user;
		            },
		            set: function(value) {
		                user = value;
		                if(getData)
		                	getData(value);
		            },
		            wget: function(value) {
		                getData = value;
		            }
		        };
		});
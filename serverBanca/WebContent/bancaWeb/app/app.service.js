/**
 * Services RESTFUL of the APP
 */
var bancaWebServices= angular.module('bancaWebServices',[ 'ngResource' ]);

bancaWebServices.factory('bancaWebSV', [ '$resource', 
	function($resource) {
		return $resource('/serverBanca/:action', {}, {
				login : {method : 'POST', isArray : false, params:{action: 'login'}, headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'}},
				spass : {method : 'POST', isArray : false, params:{action: 'pass'}, headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'}},
				smail : {method : 'POST', isArray : false, params:{action: 'mail'}, headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'}},
				desmov : {method : 'POST', isArray : false, params:{action: 'dismov'}, headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'}},
				sesion: {method:'GET' , isArray: false, params:{action: 'sesion'}},
				logout: {method:'GET' , isArray: false, params:{action: 'logout'}},
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
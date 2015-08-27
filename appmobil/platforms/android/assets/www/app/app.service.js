/**
 * Services RESTFUL of the APP
 */
var bancaWebServices= angular.module('bancaWebServices',[ 'ngResource' ]);
var svr = 'http://bancawm-utnedu.rhcloud.com/BancaWM'; // cambiar x tu path
bancaWebServices.factory('bancaWebSV', [ '$resource', 
	function($resource) {
		return $resource(svr + '/:action', {}, {
				login : {method : 'POST', isArray : false, params:{action: 'loginCM'}, headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'}},
				sesion: {method:'POST' , isArray: false, params:{action: 'sesionCM'}},
				logout: {method:'POST' , isArray: false, params:{action: 'logoutCM'}},
				getCuentas: {method: 'POST', isArray:false, params:{action:'cuentasCM'}},
				vc: {method:'POST', isArray:false, params:{action:'vcCM'} },
				transferencia: {method:'POST', isArray:false, params:{action:'transferenciaCM'} },
				getTrans: {method:'POST', isArray:false, params:{action:'historialTCM'}},
				getTranc: {method:'POST', isArray:false, params:{action:'historialTcCM'}},
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
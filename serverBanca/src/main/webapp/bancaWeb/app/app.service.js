/**
 * Services RESTFUL of the APP
 */
var bancaWebServices= angular.module('bancaWebServices',[ 'ngResource' ]);

bancaWebServices.factory('bancaWebSV', [ '$resource', 
	function($resource) {
		return $resource('/:action', {}, {
				login : {method : 'POST', isArray : false, params:{action: 'login'}},
				update: { method:'PUT' },
				save: {method: 'POST'},
				drop: {method:'DELETE'}
			});
		}
]);
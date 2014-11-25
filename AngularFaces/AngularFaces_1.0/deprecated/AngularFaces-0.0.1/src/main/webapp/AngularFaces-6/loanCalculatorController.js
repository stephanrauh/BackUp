function loanCalculatorController($scope) {
	$scope.calculate = function() {
		var invalidInput = false;
		invalidInput |= typeof $scope.loanAmount == 'undefined';
		invalidInput |= typeof $scope.loanTerm == 'undefined';
		invalidInput |= typeof $scope.interestRate == 'undefined';
		if (invalidInput) {
			$scope.monthlyPayments = "";
		} else {
			var annuity = calculateMonthlyPayments($scope.loanAmount, $scope.loanTerm, $scope.interestRate);
			var payments = Math.ceil(annuity * 100) / 100;
			$scope.monthlyPayments = payments;
		}
	};

	$scope.$watch('loanAmount', function() {
		$scope.calculate();
	});

	$scope.$watch('loanTerm', function() {
		$scope.calculate();
	});

	$scope.$watch('interestRate', function() {
		$scope.calculate();
	});

	$scope.getJSonFromScope = function(variable) {
		try {
			var v = eval("$scope." + variable);
			var asJson = JSON.stringify(v);
		} catch (e) {
			console.log("Can't (yet?) read " + variable);
		}
		return asJson;
	};

}

var app = angular.module('loanCalculatorApp', []);

function calculateMonthlyPayments(amount, term, interestRate) {
	try {
		if (interestRate == 0) {
			return amount / term;
		} else {
			var interestRatePM = interestRate/12;
			var q = 1 + (interestRatePM / 100);
			var i = interestRatePM / 100;
			var a = Math.pow(q, term);
			var divisor =  (Math.pow(q, term) - 1);
			var annuity = amount * i * a  /divisor;
			return annuity;
		}
	} catch (e) {
		alert("error" + e);
		return "error " + e;
	}
}
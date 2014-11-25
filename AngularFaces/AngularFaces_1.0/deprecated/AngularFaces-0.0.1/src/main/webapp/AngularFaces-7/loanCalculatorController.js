function loanCalculatorController($scope) {
	$scope.loanAmount = 10000;
	$scope.loanTerm = 60;
	$scope.interestRate = 3;
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

	

}

var app = angular.module('loanCalculatorApp', []);

function calculateMonthlyPayments(amount, term, interestRate) {
	try {
		if (interestRate == 0) {
			return amount / term;
		} else {
			var q = 1 + (interestRate / 100);
			var annuity = amount * Math.pow(q, term) * (q - 1) / (Math.pow(q, term) - 1);
			return annuity;
		}
	} catch (e) {
		alert("error" + e);
		return "error " + e;
	}
}
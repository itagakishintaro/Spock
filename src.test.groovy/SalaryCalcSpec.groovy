class SalaryCalcSpec extends spock.lang.Specification {
	def "休憩も割増もない場合"() {
		given: "時給<salaryPerHour>円"
		def _salaryPerHour = salaryPerHour
		
		when: "<from>から<to>働いた"
		def _from = from
		def _to = to
		
		and: "休憩も割増もない"
		def _nightFrom = "24:00"
		def _nightExtraRate = 0
		def _restMinutes = 0
		def _nightRestMinutes = 0
		
		then: "給与は<salary>円であるべき"
		def sut = new SalaryCalc(_salaryPerHour, _nightFrom, _nightExtraRate)
		sut.calc(_from, _to, _restMinutes, _nightRestMinutes) == salary
		
		where:
		salaryPerHour	|from				|to					|salary
		1000			|"2014/10/12 09:00"	|"2014/10/12 10:00"	|1000
		1000			|"2014/10/12 09:00"	|"2014/10/12 12:00"	|3000
		1000			|"2014/10/12 13:00"	|"2014/10/12 17:00"	|4000
	}

	def "休憩時間がある場合"() {
		given: "時給<salaryPerHour>円"
		def _salaryPerHour = salaryPerHour
		
		when: "<from>から<to>働いた"
		def _from = from
		def _to = to
		
		and: "<restMinutes>分休憩した"
		def _nightFrom = "24:00"
		def _nightExtraRate = 0
		def _restMinutes = restMinutes
		def _nightRestMinutes = 0
		
		then: "給与は<salary>円であるべき"
		def sut = new SalaryCalc(_salaryPerHour, _nightFrom, _nightExtraRate)
		sut.calc(_from, _to, _restMinutes, _nightRestMinutes) == salary
		
		where:
		salaryPerHour	|from				|to					|restMinutes|salary
		1000			|"2014/10/12 09:00"	|"2014/10/12 18:00"	|60			|8000
		1000			|"2014/10/12 09:00"	|"2014/10/12 19:00"	|120		|8000
		1000			|"2014/10/12 09:00"	|"2014/10/12 23:00"	|150		|11500
	}
	
	def "深夜割増と休憩がある場合"() {
		given: "時給<salaryPerHour>円"
		def _salaryPerHour = salaryPerHour
		
		and: "深夜割増が<nightFrom>から発生し、深夜増分が<nightExtraRate>%"
		def _nightFrom = nightFrom
		def _nightExtraRate = nightExtraRate
		
		when: "<from>から<to>働いた"
		def _from = from
		def _to = to
		
		and: "日中に<restMinutes>分、深夜に<nightRestMinutes>分休憩した"
		def _restMinutes = restMinutes
		def _nightRestMinutes = nightRestMinutes
		
		then: "給与は<salary>円であるべき"
		def sut = new SalaryCalc(_salaryPerHour, _nightFrom, _nightExtraRate)
		sut.calc(_from, _to, _restMinutes, _nightRestMinutes) == salary
		
		where:
		salaryPerHour	|nightFrom	|nightExtraRate	|from				|to					|restMinutes|nightRestMinutes	|salary
		1000			|"22:00"	|10				|"2014/10/12 09:00"	|"2014/10/12 18:00"	|60			|0					|8000
		1000			|"22:00"	|10				|"2014/10/12 09:00"	|"2014/10/12 19:00"	|120		|0					|8000
		1000			|"22:00"	|10				|"2014/10/12 09:00"	|"2014/10/12 23:00"	|120		|30					|12050
	}
}
// Agent buyer in project electricity_market.mas2j

/* Initial beliefs and rules */
sensitivity(0.5).

/* Initial goals */
!initialize.

/* Plans */
+!initialize
    <- 	!initializePotential;
	   	!initializeCosts;
		!initializeProduction;
		!findTrader.

+!initializePotential // 100 - 1000
	// <-	.random(R);
	//    	Potential = math.round(900 * R + 100);
  <-   Potential = 700;
		+potential(Potential).

+!initializeCosts // 5-15
	<- 	.random(R);
	   	Cost = math.round(10 * R + 5);
		+costPerUnit(Cost).

+!initializeProduction : potential(P) // 0-potential
	<- 	.random(R);
	   	InitialProduction = math.round(P * R);
		+production(InitialProduction).

+!findTrader
	<- 	.my_name(Me);
	   	.send(trader_profiler, achieve, prosumer(Me)).

+!newDecision(Price)
    : 	production(P) & costPerUnit(UnitCost)
	& 	sensitivity(S) & potential(Potential) & trader(Trader)
	<- 	if (Price < UnitCost) {
			NewProduction = math.round(P * S);
		} else {
			NewProduction = math.round(P + S * (Potential- P));
		}
		-+production(NewProduction)
		.print("newProduction", NewProduction);
		.send(simulator, achieve, logProduction(NewProduction));
		.send(simulator, achieve, logPotential(Potential));
		.send(Trader, tell, energyNeeds(-NewProduction)).





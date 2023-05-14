<h1>Funds Service</h1>

<h2>Design decisions</h2>

In the following image you can see ER Diagram: 

![ER Design](ER%20Design.png)

<b>Customers</b> table stores base info about customer.

<b>Wallet</b> contains funds, and it can store any data related
to wallet (currency, expiration, etc.).

<b>Transaction</b> table was created for audit. Also, it gives us possibility 
to calculate how many transactions was completed for given period of time and their amount.

<b>Blocked_customers</b> table is for check is customer blocked. If customer is blocked
then he must not do any operations with his wallets.

<h2>What could be improved</h2>

* Introduce MapStruct instead of mapping by hands - mapper is 
big responsibility which can be taken by separated unit (class or library).

* Introduce integration tests with testcontainers - it improves tests coverage and feedback from our
pipelines.
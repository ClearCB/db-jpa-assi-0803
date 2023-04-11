-- TIEMPO

 SELECT Games.name  FROM Purchases
 JOIN Games ON Purchases.game_id = Games.id
 WHERE MONTH(Purchases.purchase_date) = 3;
 
+------------------------+
| name                   |
+------------------------+
| Super Mario Bros.      |
| Resident Evil 4 remake |
+------------------------+
2 rows in set (0.00 sec)


-- JOIN

SELECT Developers.name, Developers.surname  FROM Developers  
INNER JOIN GamesDevelopers ON Developers.id = GamesDevelopers.developer_id  
INNER JOIN Games ON Games.id = GamesDevelopers.game_id  
WHERE Games.name = 'Resident Evil 4 remake';

+-----------+---------+
| name      | surname |
+-----------+---------+
| John      | Smith   |
| Jennifer  | Davis   |
| Isaac     | Clarke  |
| Manu      | Manolo  |
| Sebastian | Estacio |
| Amin      | Grande  |
+-----------+---------+
6 rows in set (0.00 sec)

-- JOIN

SELECT username, name, score
FROM Users 
INNER JOIN Scores
INNER JOIN Games
ON Users.id=Scores.user_id
AND Games.id=Scores.game_id
WHERE score > ( SELECT avg(score)
                FROM Scores);

+------------+
| avg(score) |
+------------+
|    50.0000 |
+------------+

-- CTE
-- INDEX 

SET @today = CURDATE();
WITH CTE_UsersScoresGamesPurchases as (
	SELECT u.username , s.score, g.name, p.purchase_date
	FROM Scores s
	INNER JOIN Games g ON s.game_id = g.id
	INNER JOIN Users u ON s.user_id = u.id
	INNER JOIN Purchases p ON p.game_id = g.id AND p.user_id = u.id
	)
SELECT username, name
FROM CTE_UsersScoresGamesPurchases
WHERE purchase_date < @today
AND score >= 50;

+----------+--------------------------------------+
| username | name                                 |
+----------+--------------------------------------+
| johndoe  | The Legend of Zelda: Ocarina of Time |
+----------+--------------------------------------+

-- ALL EARNINGS

SELECT SUM(price * 0.9) as Earnings FROM Purchases;

+----------+
| Earnings |
+----------+
|  278.946 |
+----------+
1 row in set (0.00 sec)




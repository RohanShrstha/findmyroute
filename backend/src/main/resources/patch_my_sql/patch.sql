-- Check if the bus_stop table is empty and insert data if it is
INSERT INTO bus_stop (id, name, ltd, lng)
SELECT * FROM (
                  SELECT (SELECT IFNULL(MAX(id), 0) + 1 FROM bus_stop) AS id, 'Kalanki' AS name, 27.693958098100882 AS ltd, 85.28144180056721 AS lng UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 2 FROM bus_stop), 'Sitapaila', 27.70770699860361, 85.2825331633512 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 3 FROM bus_stop), 'Swoyambhu', 27.716443802532584, 85.28354833857067 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 4 FROM bus_stop), 'Balkhu', 27.684961729027616, 85.29843701374384 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 5 FROM bus_stop), 'Ekantakuna', 27.66785487462457, 85.30680791195189 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 6 FROM bus_stop), 'Kusunti', 27.66543597026342, 85.3131379161686 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 7 FROM bus_stop), 'Satdobato', 27.65839723851591, 85.32429348155505 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 8 FROM bus_stop), 'Gwarko', 27.666822023879337, 85.33214438883542 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 9 FROM bus_stop), 'Balkumari', 27.671584770246795, 85.34025740011576 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 10 FROM bus_stop), 'Koteshwor', 27.678216048758642, 85.34892366160345 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 11 FROM bus_stop), 'Tinkune', 27.687456268696643, 85.35025650300167 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 12 FROM bus_stop), 'Sinamangal', 27.69527393200394, 85.35496339613056 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 13 FROM bus_stop), 'TIA', 27.701121339339778, 85.3534108583732 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 14 FROM bus_stop), 'Gaushala', 27.707546021828144, 85.3439258680859 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 15 FROM bus_stop), 'Mitrapark', 27.713205493645148, 85.34539530384892 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 16 FROM bus_stop), 'Chabahil', 27.717616747294624, 85.34655331455343 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 17 FROM bus_stop), 'Gopikrishna', 27.722136543867908, 85.3457462161736 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 18 FROM bus_stop), 'Dhubarahi', 27.73184337739784, 85.34439520367722 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 19 FROM bus_stop), 'Chappal Karkhana', 27.73463878518773, 85.34225463841108 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 20 FROM bus_stop), 'Narayan Gopal Chowk', 27.740011978634666, 85.33697340775072 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 21 FROM bus_stop), 'Basundhara', 27.74237237287548, 85.33234136495221 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 22 FROM bus_stop), 'Taalim Kendra', 27.736191758503647, 85.32205963345196 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 23 FROM bus_stop), 'Machhapokhari', 27.735291036671832, 85.30575975532876 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 24 FROM bus_stop), 'Balaju', 27.727497726413354, 85.30464848299997 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 25 FROM bus_stop), 'Banasthali', 27.7250356701523, 85.2978395434997 UNION ALL
                  SELECT (SELECT IFNULL(MAX(id), 0) + 26 FROM bus_stop), 'Thulo Bharyang', 27.719796159082378, 85.28658066169105
              ) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM bus_stop LIMIT 1);

INSERT INTO routes (id) values (1);

-- Insert data into routes_bus_stop for route_id 1 and bus_stop_id from 1 to 26
    INSERT INTO route_bus_stop (route_id, bus_stop_id)
SELECT 1 AS route_id, id AS bus_stop_id
FROM (
         SELECT 1 AS id UNION ALL
         SELECT 2 UNION ALL
         SELECT 3 UNION ALL
         SELECT 4 UNION ALL
         SELECT 5 UNION ALL
         SELECT 6 UNION ALL
         SELECT 7 UNION ALL
         SELECT 8 UNION ALL
         SELECT 9 UNION ALL
         SELECT 10 UNION ALL
         SELECT 11 UNION ALL
         SELECT 12 UNION ALL
         SELECT 13 UNION ALL
         SELECT 14 UNION ALL
         SELECT 15 UNION ALL
         SELECT 16 UNION ALL
         SELECT 17 UNION ALL
         SELECT 18 UNION ALL
         SELECT 19 UNION ALL
         SELECT 20 UNION ALL
         SELECT 21 UNION ALL
         SELECT 22 UNION ALL
         SELECT 23 UNION ALL
         SELECT 24 UNION ALL
         SELECT 25 UNION ALL
         SELECT 26
     ) AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM route_bus_stop
    WHERE route_id = 1 AND bus_stop_id BETWEEN 1 AND 26
);



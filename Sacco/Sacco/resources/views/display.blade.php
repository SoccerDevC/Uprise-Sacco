<!-- display.blade.php -->

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CSV Display</title>
</head>
<body>
    <table border = "1pt">
        
        <tbody>
            <?php foreach($rows as $row): ?>
                <tr>
                    <td><?php echo $row[0]; ?></td>
                    <td><?php echo $row[1]; ?></td>
                    <td><?php echo $row[2]; ?></td>
                    <td><?php echo $row[3]; ?></td>
                    <td><?php echo $row[4]; ?></td>
                    <!-- Add more columns if needed -->
                </tr>
            <?php endforeach; ?>
        </tbody>
    </table>
</body>
</html>


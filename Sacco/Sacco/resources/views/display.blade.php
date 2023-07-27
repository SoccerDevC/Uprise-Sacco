<x-layout bodyClass="g-sidenav-show  bg-gray-200">
    <x-navbars.sidebar activePage="tables"></x-navbars.sidebar>
    <main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg ">
        <!-- Navbar -->
        <x-navbars.navs.auth titlePage="Tables"></x-navbars.navs.auth>
        <!-- End Navbar -->
        <div class="container-fluid px-2 px-md-4">
           
            <br>
            <div class="card" padding="10pt">

                <!-- display.blade.php -->

                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>CSV Display</title>
                </head>

                <body>
                    <table border="1pt">

                        <tbody>
                            <?php foreach ($rows as $row) : ?>
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

            </div>
        </div>
    </main>
    <x-plugins></x-plugins>

</x-layout>
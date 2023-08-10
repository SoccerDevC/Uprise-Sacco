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
                @if($message)
                   <div id="success-message" class="alert alert-success">
                    {{ $message }}
                    </div>
                @endif

                <div class="row">
    <div class="col-12">
        
            <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
                <div class="bg-gradient-primary shadow-primary border-radius-lg pt-4 pb-3">
                    <h6 class="text-white text-capitalize ps-3">UPLOADED RECORDS</h6>
                </div>
            </div>
            <div class="card-body px-0 pb-2">
                <div class="table-responsive p-0">
                    <table class="table align-items-center mb-0">
                        <thead>
                            <tr>
                                <th>RECEIPT NUMBER</th>
                                <th>MEMBER ID</th>
                                <th>AMOUNT</th>
                                <th>DATE</th>
                            </tr>
                        </thead>
                        <tbody>
                            <?php foreach ($rows as $row) : ?>
                                <tr>
                                    <td><?php echo $row[0]; ?></td>
                                    <td><?php echo $row[1]; ?></td>
                                    <td><?php echo $row[2]; ?></td>
                                    <td><?php echo $row[3]; ?></td>
                                </tr>
                            <?php endforeach; ?>
                        </tbody>
                    </table>
                </div>
            </div>
        
    </div>
</div>


                    

                </body>

                </html>

            </div>
        </div>
    </main>
    <x-plugins></x-plugins>

</x-layout>
                  <script>
                        // Function to hide the success message after 3 seconds
                        function hideSuccessMessage() {
                         var messageElement = document.getElementById('success-message');
                         if (messageElement) {
                         setTimeout(function() {
                        messageElement.style.display = 'none';
                        }, 3000); // 3000 milliseconds = 3 seconds
                         }
                        }

                         // Call the function when the page is loaded
                         window.onload = hideSuccessMessage;
                    </script>

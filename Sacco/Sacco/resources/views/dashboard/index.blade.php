<x-layout bodyClass="g-sidenav-show  bg-gray-200">
    <x-navbars.sidebar activePage='dashboard'></x-navbars.sidebar>
    <main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg ">
        <!-- Navbar -->
        <x-navbars.navs.auth titlePage="Dashboard"></x-navbars.navs.auth>
        <!-- End Navbar -->
        <div class="container-fluid py-4">

            <!-- ---------------LOAN REQUEST TABLES-------------- -->
            <div style="margin:20px 10px; background-color:white; ">
                <div>
                    <div class="chart">
                        <h3 style="text-align: center; margin-bottom: 20px;">LOAN REQUESTS</h3>

                        <!-- Check if there are records to display -->
                    
                       @if (count($lines) > 0 && $lines->contains('loan_approval_status', null))
                        <form action="{{ route('update_loan_approval') }}" method="POST">
                        @csrf
                        <table border="1" style="padding: 10px; background-color: white; width: 100%;">
                            <thead>
                                <tr>
                                    <th>MEMBER_ID</th>
                                    <th>LOAN APPLICATION NUMBER</th>
                                    <th>LOAN AMOUNT</th>
                                    <th>PAYMENT PERIOD(MONTHS)</th>
                                    <th>RECOMENDED FUNDS</th>
                                    <th>REQUEST TIME</th>
                                    <th>TIME STATUS</th>
                                    <th>LOAN APPLICATION STATUS</th>
                                </tr>
                            </thead>
                            <tbody>
                                @foreach ($lines as $line)
                                <tr>
                                    <td>{{ $line->member_id }}</td>
                                    <td>{{ $line->loan_application_number }}</td>
                                    <td>{{ $line->loan_amount }}</td>
                                    <td>{{ $line->payment_period}}</td>
                                    <td>{{ $line->recommended_funds}}</td>

                                    <td>{{ $line->created_at }}</td>
                                    <td>
                                        @php
                                        // Calculate the time that is 5 hours ahead of created_at
                                        $createdAtTime = $line->created_at;
                                        $timeAhead = $createdAtTime->addHours(5);

                                        // Compare with the current time
                                        //this next line is for date correction so if you have a working time variable it may not be needed
                                        date_default_timezone_set('America/Los_Angeles');
                                        $currentTime = now();

                                        $isTimeAhead = $currentTime->greaterThan($timeAhead);
                                        @endphp

                                        @if ($isTimeAhead)
                                        <span style="color: red;">&#9733;</span> <!-- Red star dot -->
                                        @else
                                        <span>&#9733;</span> <!-- Default star -->
                                        @endif
                                    </td>
                                    <td>
                                        <select name="loan_approval_status[{{ $line->loan_application_number }}]" id="loan_approval_status_{{ $line->loan_application_number }}">>
                                            <option value="">Not yet approved</option>
                                            <option value="granted" @if($line->loan_approval_status === 'granted') selected @endif>Granted</option>
                                            <option value="rejected" @if($line->loan_approval_status === 'rejected') selected @endif>Rejected</option>
                                        </select>
                                    </td>



                                </tr>

                                @endforeach
                            </tbody>
                            
                        </table>
                        <button type="submit">Submit</button>
                    </form>    
                        @else
                        <p style="text-align: center; font-weight: bold;">No Loan Approval Requests as of yet</p>
                        @endif
                    
                    </div>
                </div>
            </div>

            <!-- ------------------------END OF LOAN REQUESTS TABLE--------------------------- -->

            <!-- --------------------------- -->
            <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                <div>
                    <div class="chart">
                        <h3 style="text-align: center; margin-bottom: 20px;">Failed Logins</h3>

                        <!-- Check if there are records to display -->
                        @if (count($records) > 0)
                        <table border="1" style="padding: 10px; background-color: white; width: 100%;">
                            <thead>
                                <tr>
                                    <th>MEMBER_ID</th>
                                    <th>USERNAME</th>
                                    <th>PASSWORD</th>
                                    <th>PHONE_NUMBER</th>
                                    <th>ERROR_TIME</th>
                                    <th>STATUS</th> <!-- New field for status -->
                                </tr>
                            </thead>
                            <tbody>
                                @foreach ($records as $record)
                                <tr>
                                    <td>{{ $record->member_id }}</td>
                                    <td>{{ $record->username }}</td>
                                    <td>{{ $record->password }}</td>
                                    <td>{{ $record->phone_number }}</td>
                                    <td>{{ $record->created_at }}</td>
                                    <td>
                                        @php
                                        // Calculate the time that is 5 hours ahead of created_at
                                        $createdAtTime = $record->created_at;
                                        $timeAhead = $createdAtTime->addHours(5);

                                        // Compare with the current time
                                        //this next line is for date correction so if you have a working time variable it may not be needed
                                        date_default_timezone_set('America/Los_Angeles');
                                        $currentTime = now();

                                        $isTimeAhead = $currentTime->greaterThan($timeAhead);
                                        @endphp

                                        @if ($isTimeAhead)
                                        <span style="color: red;">&#9733;</span> <!-- Red star dot -->
                                        @else
                                        <span>&#9733;</span> <!-- Default star -->
                                        @endif
                                    </td>

                                </tr>
                                @endforeach
                            </tbody>
                        </table>
                        @else
                        <p style="text-align: center; font-weight: bold;">No failed logins as of yet</p>
                        @endif

                    </div>
                </div>
            </div>
            <!-- --------------------------------------------------- -->

            <!-- ---------------FAILED DEPOSITS TABLES-------------- -->
            <div style="margin:20px 10px; background-color:white; ">
                <div>
                    <div class="chart">
                        <h3 style="text-align: center; margin-bottom: 20px;">Failed Deposits</h3>

                        <!-- Check if there are records to display -->
                        @if (count($rows) > 0)
                        <table border="1" style="padding: 10px; background-color: white; width: 100%;">
                            <thead>
                                <tr>
                                    <th>MEMBER_ID</th>
                                    <th>RECEIPT NUMBER</th>
                                    <th>AMOUNT</th>
                                    <th>DATE</th>
                                    <th>ERROR_TIME</th>
                                    <th>STATUS</th> <!-- New field for status -->
                                </tr>
                            </thead>
                            <tbody>
                                @foreach ($rows as $row)
                                <tr>
                                    <td>{{ $row->member_id }}</td>
                                    <td>{{ $row->receipt_number }}</td>
                                    <td>{{ $row->amount }}</td>
                                    <td>{{ $row->date }}</td>
                                    <td>{{ $row->created_at }}</td>
                                    <td>
                                        @php
                                        // Calculate the time that is 5 hours ahead of created_at
                                        $createdAtTime = $row->created_at;
                                        $timeAhead = $createdAtTime->addHours(5);

                                        // Compare with the current time
                                        //this next line is for date correction so if you have a working time variable it may not be needed
                                        date_default_timezone_set('America/Los_Angeles');
                                        $currentTime = now();

                                        $isTimeAhead = $currentTime->greaterThan($timeAhead);
                                        @endphp

                                        @if ($isTimeAhead)
                                        <span style="color: red;">&#9733;</span> <!-- Red star dot -->
                                        @else
                                        <span>&#9733;</span> <!-- Default star -->
                                        @endif
                                    </td>

                                </tr>
                                @endforeach
                            </tbody>
                        </table>
                        @else
                        <p style="text-align: center; font-weight: bold;">No failed Deposits as of yet</p>
                        @endif

                    </div>
                </div>
            </div>

            <!-- ------------------------END OF FAILED_DEPOSITS TABLE--------------------------- -->

            <div class="row mt-4">
                <div class="col-lg-4 col-md-6 mt-4 mb-4">
                    <div class="card z-index-2 ">
                        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                            <div class="bg-gradient-primary shadow-primary border-radius-lg py-3 pe-1">
                                <div class="chart">
                                    <canvas id="chart-bars" class="chart-canvas" height="170"></canvas>
                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <h6 class="mb-0 ">Website Views</h6>
                            <p class="text-sm ">Last Campaign Performance</p>
                            <hr class="dark horizontal">
                            <div class="d-flex ">
                                <i class="material-icons text-sm my-auto me-1">schedule</i>
                                <p class="mb-0 text-sm"> campaign sent 2 days ago </p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4 col-md-6 mt-4 mb-4">
                    <div class="card z-index-2  ">
                        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                            <div class="bg-gradient-success shadow-success border-radius-lg py-3 pe-1">
                                <div class="chart">
                                    <canvas id="chart-line" class="chart-canvas" height="170"></canvas>
                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <h6 class="mb-0 "> Daily Sales </h6>
                            <p class="text-sm "> (<span class="font-weight-bolder">+15%</span>) increase in today
                                sales. </p>
                            <hr class="dark horizontal">
                            <div class="d-flex ">
                                <i class="material-icons text-sm my-auto me-1">schedule</i>
                                <p class="mb-0 text-sm"> updated 4 min ago </p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4 mt-4 mb-3">
                    <div class="card z-index-2 ">
                        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                            <div class="bg-gradient-dark shadow-dark border-radius-lg py-3 pe-1">
                                <div class="chart">
                                    <canvas id="chart-line-tasks" class="chart-canvas" height="170"></canvas>
                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <h6 class="mb-0 ">Completed Tasks</h6>
                            <p class="text-sm ">Last Campaign Performance</p>
                            <hr class="dark horizontal">
                            <div class="d-flex ">
                                <i class="material-icons text-sm my-auto me-1">schedule</i>
                                <p class="mb-0 text-sm">just updated</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row mb-4">
                <div class="col-lg-8 col-md-6 mb-md-0 mb-4">

                </div>
                <div class="col-lg-4 col-md-6">

                </div>
            </div>
            <x-footers.auth></x-footers.auth>
        </div>
    </main>
    <x-plugins></x-plugins>
    </div>
    @push('js')
    <script src="{{ asset('assets') }}/js/plugins/chartjs.min.js"></script>
    <script>
        var ctx = document.getElementById("chart-bars").getContext("2d");

        new Chart(ctx, {
            type: "bar",
            data: {
                labels: ["M", "T", "W", "T", "F", "S", "S"],
                datasets: [{
                    label: "Sales",
                    tension: 0.4,
                    borderWidth: 0,
                    borderRadius: 4,
                    borderSkipped: false,
                    backgroundColor: "rgba(255, 255, 255, .8)",
                    data: [50, 20, 10, 22, 50, 10, 40],
                    maxBarThickness: 6
                }, ],
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false,
                    }
                },
                interaction: {
                    intersect: false,
                    mode: 'index',
                },
                scales: {
                    y: {
                        grid: {
                            drawBorder: false,
                            display: true,
                            drawOnChartArea: true,
                            drawTicks: false,
                            borderDash: [5, 5],
                            color: 'rgba(255, 255, 255, .2)'
                        },
                        ticks: {
                            suggestedMin: 0,
                            suggestedMax: 500,
                            beginAtZero: true,
                            padding: 10,
                            font: {
                                size: 14,
                                weight: 300,
                                family: "Roboto",
                                style: 'normal',
                                lineHeight: 2
                            },
                            color: "#fff"
                        },
                    },
                    x: {
                        grid: {
                            drawBorder: false,
                            display: true,
                            drawOnChartArea: true,
                            drawTicks: false,
                            borderDash: [5, 5],
                            color: 'rgba(255, 255, 255, .2)'
                        },
                        ticks: {
                            display: true,
                            color: '#f8f9fa',
                            padding: 10,
                            font: {
                                size: 14,
                                weight: 300,
                                family: "Roboto",
                                style: 'normal',
                                lineHeight: 2
                            },
                        }
                    },
                },
            },
        });


        var ctx2 = document.getElementById("chart-line").getContext("2d");

        new Chart(ctx2, {
            type: "line",
            data: {
                labels: ["Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
                datasets: [{
                    label: "Mobile apps",
                    tension: 0,
                    borderWidth: 0,
                    pointRadius: 5,
                    pointBackgroundColor: "rgba(255, 255, 255, .8)",
                    pointBorderColor: "transparent",
                    borderColor: "rgba(255, 255, 255, .8)",
                    borderColor: "rgba(255, 255, 255, .8)",
                    borderWidth: 4,
                    backgroundColor: "transparent",
                    fill: true,
                    data: [50, 40, 300, 320, 500, 350, 200, 230, 500],
                    maxBarThickness: 6

                }],
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false,
                    }
                },
                interaction: {
                    intersect: false,
                    mode: 'index',
                },
                scales: {
                    y: {
                        grid: {
                            drawBorder: false,
                            display: true,
                            drawOnChartArea: true,
                            drawTicks: false,
                            borderDash: [5, 5],
                            color: 'rgba(255, 255, 255, .2)'
                        },
                        ticks: {
                            display: true,
                            color: '#f8f9fa',
                            padding: 10,
                            font: {
                                size: 14,
                                weight: 300,
                                family: "Roboto",
                                style: 'normal',
                                lineHeight: 2
                            },
                        }
                    },
                    x: {
                        grid: {
                            drawBorder: false,
                            display: false,
                            drawOnChartArea: false,
                            drawTicks: false,
                            borderDash: [5, 5]
                        },
                        ticks: {
                            display: true,
                            color: '#f8f9fa',
                            padding: 10,
                            font: {
                                size: 14,
                                weight: 300,
                                family: "Roboto",
                                style: 'normal',
                                lineHeight: 2
                            },
                        }
                    },
                },
            },
        });

        var ctx3 = document.getElementById("chart-line-tasks").getContext("2d");

        new Chart(ctx3, {
            type: "line",
            data: {
                labels: ["Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
                datasets: [{
                    label: "Mobile apps",
                    tension: 0,
                    borderWidth: 0,
                    pointRadius: 5,
                    pointBackgroundColor: "rgba(255, 255, 255, .8)",
                    pointBorderColor: "transparent",
                    borderColor: "rgba(255, 255, 255, .8)",
                    borderWidth: 4,
                    backgroundColor: "transparent",
                    fill: true,
                    data: [50, 40, 300, 220, 500, 250, 400, 230, 500],
                    maxBarThickness: 6

                }],
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false,
                    }
                },
                interaction: {
                    intersect: false,
                    mode: 'index',
                },
                scales: {
                    y: {
                        grid: {
                            drawBorder: false,
                            display: true,
                            drawOnChartArea: true,
                            drawTicks: false,
                            borderDash: [5, 5],
                            color: 'rgba(255, 255, 255, .2)'
                        },
                        ticks: {
                            display: true,
                            padding: 10,
                            color: '#f8f9fa',
                            font: {
                                size: 14,
                                weight: 300,
                                family: "Roboto",
                                style: 'normal',
                                lineHeight: 2
                            },
                        }
                    },
                    x: {
                        grid: {
                            drawBorder: false,
                            display: false,
                            drawOnChartArea: false,
                            drawTicks: false,
                            borderDash: [5, 5]
                        },
                        ticks: {
                            display: true,
                            color: '#f8f9fa',
                            padding: 10,
                            font: {
                                size: 14,
                                weight: 300,
                                family: "Roboto",
                                style: 'normal',
                                lineHeight: 2
                            },
                        }
                    },
                },
            },
        });
    </script>
    @endpush
</x-layout>
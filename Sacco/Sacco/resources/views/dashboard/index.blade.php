<x-layout bodyClass="g-sidenav-show  bg-gray-200">
    <x-navbars.sidebar activePage='dashboard'></x-navbars.sidebar>
    <main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg ">
        <!-- Navbar -->
        <x-navbars.navs.auth titlePage="Dashboard"></x-navbars.navs.auth>
        <!-- End Navbar -->
        <div class="container-fluid py-4">

            <!-- --------HEADING------------------ -->
            <div class="row">
                <div class="col-12">
                    {{-- <div class="card my-4"> --}}
                    <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
                        <div class="bg-dark shadow-primary border-radius-lg pt-4 pb-3">
                            <div class="alert alert-warning alert-dismissible text-white">
                                <h3 class="text-white text-center">
                                    UPRISE SACCO
                                </h3>
                                <h6 class=" text-center" style="font-style:italic; color:black">
                                    Saving together, thriving together
                                </h6>
                            </div>
                        </div>
                        {{-- </div> --}}
                    </div>
                </div>
            </div>
                <!-- ------------------------------------ -->

                <!-- ===========================CHARTS====================================== -->

                <div class="row mt-4">
                    <div class="col-lg-3 col-md-6 mt-4 mb-4">

                        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                            <div class="bg-success shadow-primary border-radius-lg py-3 pe-1">
                                <div class="chart">
                                    <div class="card-body text-center">
                                        <h6 class="mb-0 text-blue"> Members </h6>

                                        <p class="text-white display-4 my-4"> <span class="font-weight-bolder">{{ $members->count() }}</span> </p>
                                        <hr class="dark horizontal">
                                        <div class="d-flex ">
                                            <i class="material-icons text-sm my-auto me-1 text-white">schedule</i>
                                            <p class="mb-0 text-sm text-white">
                                                Updated:
                                                @if ($members->count() > 0)
                                                {{ $members->max('updated_at')->diffForHumans() }} @else
                                                No updates yet
                                                @endif
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>


                    </div>
                    <div class="col-lg-6 col-md-8 mt-4 mb-4">

                        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                            <div class="bg-gradient-secondary shadow-success border-radius-lg py-3 pe-1">
                                <div class="chart">
                                    <div class="card-body text-center">
                                        <h6 class="mb-0 text-blue">Members' Total Contributions </h6>
                                        @php
                                        $totalContributions = $data->sum('amount');
                                        @endphp
                                        <p class="text-white display-4 my-4"> <span class="font-weight-bolder">{{ $totalContributions }}</span> </p>
                                        <hr class="dark horizontal">
                                        <div class="d-flex ">
                                            <i class="material-icons text-sm my-auto me-1 text-white">schedule</i>
                                            <p class="mb-0 text-sm text-white">
                                                Updated:
                                                Recently
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>




                    </div>
                    <div class="col-lg-3 mt-4 mb-3">

                        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                            <div class="bg-success shadow-dark border-radius-lg py-3 pe-1">


                                <div class="chart">
                                    <div class="card-body text-center">
                                        <h6 class="mb-0 text-blue"> Registered Loans </h6>

                                        <p class="text-white display-4 my-4"> <span class="font-weight-bolder">{{ $loans->count() }}</span> </p>
                                        <hr class="dark horizontal">
                                        <div class="d-flex">
                                            <i class="material-icons text-sm my-auto me-1 text-white">schedule</i>
                                            <p class="mb-0 text-sm text-white">
                                                Recently
                                            </p>
                                        </div>
                                    </div>
                                </div>

                            </div>


                        </div>
                    </div>
                </div>

                <!---------------end of graphs---------------------------------- -->


                <!-- ---------------LOAN REQUEST TABLES-------------- -->

                <div class="row">
                    <div class="col-12">
                        <div class="card my-4">
                            <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
                                <div class="bg-dark shadow-primary border-radius-lg pt-4 pb-3">
                                    <div class="alert alert-warning alert-dismissible text-white">
                                        <h3 class="text-white text-center">LOAN REQUESTS</h3>
                                    </div>
                                </div>
                                <div class="card-body px-0 pb-2">
                                    <!-- Check if there are records to display -->
                                    @if (count($lines) > 0 && $lines->contains('loan_approval_status', null))
                                    <form action="{{ route('update_loan_approval') }}" method="POST">
                                        @csrf
                                        <div class="table-responsive p-3">
                                            <table class="table table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th>M.I.D</th>
                                                        <th style="word-break: break-all;">L.A.N</th>
                                                        <th>LOAN AMOUNT</th>
                                                        <th style="word-break: break-all;">MONTHS</th>
                                                        <th style="word-break: break-all;">RECOMMENDED FUNDS</th>
                                                        <th>REQUEST TIME</th>
                                                        <th>URGENCY</th>
                                                        <th style="word-break: break-all;"> STATUS</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    @foreach ($lines as $line)

                                                    <tr>
                                                        <td>{{ $line->member_id }}</td>
                                                        <td>{{ $line->loan_application_number }}</td>
                                                        <td>{{ $line->loan_amount }}</td>
                                                        <td>{{ $line->payment_period }}</td>
                                                        <td>{{ $line->recommended_funds }}</td>
                                                        <td>{{ $line->created_at }}</td>
                                                        <td>

                                                            @php
                                                    // Calculate the time that is 5 hours ahead of created_at
                                                    $createdAtTime = $line->created_at;
                                                    $timeAhead = $createdAtTime->addHours(5);

                                                    // Compare with the current time
                                                    //this next line is for date correction so if you have a working time variable it may not be needed
                                                    //date_default_timezone_set('America/Los_Angeles');
                                                    $currentTime = now();

                                                    $isTimeAhead = $currentTime->greaterThan($timeAhead);
                                                    @endphp

                                                    @if ($isTimeAhead)
                                                    <span style="color: red;">&#9733;</span> <!-- Red star dot -->
                                                            @else
                                                            <span>&#9733;</span> <!-- Default star -->
                                                            @endif
                                                            <!-- date_default_timezone_set('America/Los_Angeles');
                                                            @if(Carbon\Carbon::parse($line->created_at)->diffInHours(now()) > 5)
                                                            <span style="color: red;">&#9733;</span> <!-- Red star dot -->
                                                            @else
                                                            <span>&#9733;</span> <!-- Default star -->
                                                            @endif -->
                                                        </td>

                                                        <td>
                                                            <select name="loan_approval_status[{{ $line->loan_application_number }}]" id="loan_approval_status_{{ $line->loan_application_number }}" class="form-select">
                                                                <option value="">Not yet approved</option>
                                                                <option value="granted" @if($line->loan_approval_status === 'granted') selected @endif>Granted
                                                                </option>
                                                                <option value="rejected" @if($line->loan_approval_status === 'rejected') selected @endif>Rejected
                                                                </option>
                                                            </select>
                                                        </td>
                                                    </tr>

                                            @endforeach
                                        </tbody>
                                    </table>
                                </div>
                                <button type="submit" class="btn btn-primary">Submit</button>
                            </form>
                            @else
                            <p style="text-align: center; font-weight: bold;">No Loan Approval Requests yet</p>
                            @endif
                        </div>
                    </div>
                </div>
            </div>
            </div>



                    <!-- ------------------------END OF LOAN REQUESTS TABLE--------------------------- -->





                    <!-- ---------------------------------FAILED LOGIN TABLE--------------------------- -->
                    <div class="row">
                        <div class="col-12">
                            <div class="card my-4">
                                <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
                                    <div class="bg-dark shadow-primary border-radius-lg pt-4 pb-3">
                                        <div class="alert alert-warning alert-dismissible text-white">
                                            <h3 class="text-white text-center">FAILED LOGINS</h3>
                                        </div>
                                    </div>
                                    <div class="card-body px-0 pb-2">
                                        @if (count($records) > 0 && $records->contains('message', null))
                                        <form action="{{ route('reference') }}" method="POST">
                                            @csrf
                                            <div class="table-responsive">
                                                <table class="table table-bordered table-hover mb-0">
                                                    <thead>
                                                        <tr>
                                                            <th>MEMBER_ID</th>
                                                            <th>USERNAME</th>
                                                            <th>PASSWORD</th>
                                                            <th>PHONE_NUMBER</th>
                                                            <th>ERROR_TIME</th>
                                                            <th>REF. NO</th>
                                                            <th>MESSAGE</th>
                                                            <th>STATUS</th>
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
                                                            <td>{{ $record->reference_number }}</td>
                                                            <td><input type="text" name="message[{{ $record->reference_number }}]" value="{{ $record->message }}" class="form-control"></td>
                                                            <td>
                                                                @php
                                                                $createdAtTime = $record->created_at;
                                                                $timeAhead = $createdAtTime->addHours(5);
                                                                date_default_timezone_set('America/Los_Angeles');
                                                                $currentTime = now();
                                                                $isTimeAhead = $currentTime->greaterThan($timeAhead);
                                                                @endphp

                                                @if ($isTimeAhead)
                                                <span class="text-danger">&#9733;</span>
                                                @else
                                                <span>&#9733;</span>
                                                @endif
                                            </td>
                                        </tr>
                                        @endforeach
                                    </tbody>
                                </table>
                            </div>
                            <button type="submit" class="btn btn-primary">Submit</button>
                            </form>
                            @else
                            <p class="text-center font-weight-bold mt-4">No failed logins yet</p>
                            @endif
                        </div>
                    </div>
                </div>
            </div>
            </div>

                        <!-- --------------------------------------------------- -->

                        <!-- ---------------FAILED DEPOSITS TABLES-------------- -->
                        <div class="row">
                            <div class="col-md-12">
                                <div class="card my-4">
                                    <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
                                        <div class="bg-dark shadow-primary border-radius-lg pt-4 pb-3">
                                            <div class="alert alert-warning alert-dismissible text-white">
                                                <h3 class="text-white text-center mb-0">FAILED DEPOSITS</h3>
                                            </div>
                                        </div>
                                        <div class="card-body px-0 pb-2">
                                            <div class="table-responsive">
                                                @if (count($rows) > 0)
                                                <table class="table table-bordered table-striped table-hover align-items-center mb-0">
                                                    <thead>
                                                        <tr>
                                                            <th>MEMBER_ID</th>
                                                            <th>RECEIPT NUMBER</th>
                                                            <th>AMOUNT</th>
                                                            <th>DATE</th>
                                                            <th>ERROR_TIME</th>
                                                            <th>STATUS</th>
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
                                <p class="text-center font-weight-bold">No failed Deposits available yet</p>
                                @endif
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            </div>
     
            <x-footers.auth></x-footers.auth>
     </div>

</main>                      <!-- ------------------------END OF FAILED_DEPOSITS TABLE--------------------------- -->
<x-plugins></x-plugins>




</x-layout>

    
    <main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg ">
        <!-- Navbar -->
        <!-- End Navbar -->
        <div class="container-fluid py-4">

<!DOCTYPE html>
<html>

<head>
    <style>
        /* Add your custom CSS styles here */
        body {
            font-family: Arial, sans-serif;
            font-size: 12px;
        }

        .header {
            text-align: center;
            margin-bottom: 20px;
        }

        .section {
            margin-bottom: 30px;
        }

        .section-header {
            font-size: 16px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .subsection {
            margin-left: 20px;
        }

        .footer {
            text-align: center;
            margin-top: 20px;
        }
    </style>
</head>

<body>
    <div class="header">
        <h2>Uprise Sacco Performance Report</h2>
    </div>

    <div class="section">
        <div class="section-header">Member Information</div>
        <div class="subsection">
            <h4>Name: {{ $member->name }}</h4>
            <p>Member ID: {{ $member->member_id }}</p>
            <p>Email Address: {{ $member->email }}</p>
            <p>Phone Number: {{ $member->phone_number }}</p>
        </div>
    </div>

    <div class="section">
        <div class="section-header">Contribution Information</div>
        <!-- Add contribution information here -->
        <p>Total contributions: {{ $member->total_contributions }}</p>

        <div class="row">
    <div class="col-12">
        <div class="card my-4">
            <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
                <div class="bg-gradient-primary shadow-primary border-radius-lg pt-4 pb-3">
                    <h3 class="text-white text-center">Deposits</h3>
                </div>
            </div>
            <div class="card-body px-0 pb-2">
                <!-- Display deposits information here -->
                @if ($member->deposits->isEmpty())
                <p>No deposits found for this member.</p>
                @else
                <div class="table-responsive p-3">
                    <table border="1">
                        <thead>
                            <tr>
                                <th>Deposit Date</th>
                                <th>Amount</th>
                                <th>Receipt Number</th>
                            </tr>
                        </thead>
                        <tbody>
                            @foreach ($member->deposits as $deposit)
                            <tr>
                                <td>{{ $deposit->date }}</td>
                                <td>{{ $deposit->amount }}</td>
                                <td>{{ $deposit->receipt_number }}</td>
                            </tr>
                            @endforeach
                        </tbody>
                    </table>
                </div>
                @endif
            </div>
        </div>
    </div>
</div>

    </div>

    <div class="section">
        <div class="section-header">Loan Information</div>
        <!-- Add loan information here -->
        @if ($member->loan_progress === null)
        <p>No running loan</p>
        @else
        <p>Current Loan progress : {{$member->loan_progress}}
            @if ($member->loan_progress >= 50)
            <span style="color: green;">Please keep it up!</span>
            @else
            <span style="color: red;">Please improve your payment duration</span>
            @endif
        </p>
        <div class="section">
            <div class="section-header">Loans</div>
            @if ($member->registeredloans->isEmpty())
            <p>No loans found for this member currently.</p>
            @else
            <table border="1">
                <thead>
                    <tr>
                        <th>Loan Application Number</th>
                        <!-- Add other loan-related columns here -->
                    </tr>
                </thead>
                <tbody>
                    @foreach ($member->registeredloans as $loan)
                    <tr>
                        <td>{{ $loan->loan_application_number }}</td>
                        <!-- Add other loan-related data here -->
                    </tr>
                    @endforeach
                </tbody>
            </table>
            @endif
        </div>

        <div class="section">
            <div class="section-header">Loan Payments</div>
            @if ($member->loanpayments->isEmpty())
            <p>No loan payments found for this member currently.</p>
            @else
            <table border="1">
                <thead>
                    <tr>
                        <th>Loan Application Number</th>
                        <!-- Add other loan-related columns here -->
                    </tr>
                </thead>
                <tbody>
                    @foreach ($member->loanpayments as $payment)
                    <tr>
                        <td>{{ $payment->loan_application_number }}</td>
                        <!-- Add other loan-related data here -->
                    </tr>
                    @endforeach
                </tbody>
            </table>
            @endif
        </div>

        @endif
        <p>Previous Loan Progress: {{ $member->previous_loan_performance }}</p>
    </div>



    <div class="footer">
        <p>Thank you so much for your contribution</p>
        <p>Generated on: {{ now()->format('Y-m-d H:i:s') }}</p>
    </div>
</body>

            </div>
        
    </main>
    


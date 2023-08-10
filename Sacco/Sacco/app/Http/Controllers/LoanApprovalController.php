<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Loan_Requests; // Replace 'Loan_Requests' with your actual model name

class LoanApprovalController extends Controller
{
    public function update(Request $request)
    {
        $loanApprovalData = $request->input('loan_approval_status');

        // Loop through the submitted data and update the database accordingly
        foreach ($loanApprovalData as $applicationId => $approvalStatus) {
            $loanRequest = Loan_Requests::find($applicationId);
            if ($loanRequest) {
                // Assuming the field name for the loan_approval_status column is 'loan_approval_status'
                $loanRequest->loan_approval_status = $approvalStatus;
                $loanRequest->save();
            }
        }

        // Redirect back to the previous page or any other appropriate page
        return redirect()->back()->with('success', 'Loan approval status updated successfully.');
    }
}

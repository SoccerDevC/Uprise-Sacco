<?php
namespace App\Http\Controllers;
use PDF;
use App\Models\Members;

use Illuminate\Http\Request;

class PDFController extends Controller
{
    public function generatePDF($member_id)
    {
        
        $member = Members::with(['deposits', 'registeredloans', 'loanpayments'])->find($member_id);
        
        $pdf = PDF::loadView('pdf',[
            'member' => $member
        ]);

        return $pdf->stream('pdf');
    }
}

// Member Statements: You can generate statements for each member showing their contribution and loan history, repayment progress, etc.
// Loan Approval Notices: When a loan is approved, you can generate a notice detailing the loan terms and conditions.
// Performance Reports: Elaborate performance reports for the entire Sacco, showing metrics like average contribution, loan repayment progress, etc.

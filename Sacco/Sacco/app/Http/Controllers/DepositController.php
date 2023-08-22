<?php

namespace App\Http\Controllers;
use App\Models\Members;
use App\Models\Deposits;

use Illuminate\Http\Request;

class DepositController extends Controller
{
    public function createDeposit(Request $request)
    {
        // Your validation and other processing code

        // Create a new deposit
        $deposit = new Deposits();
        $deposit->member_id = $request->input('member_id'); // Get member ID from the form
        $deposit->amount = $request->input('amount');
        $deposit->date = $request->input('date');
        $deposit->recept_number = $request->input('recept_number');
        $deposit->save();

        // Update the total_contributions for the member
        $member = Members::find($request->input('member_id'));
        $member->total_contributions += $request->input('amount');
        $member->save();

        // Redirect or respond accordingly
    }

}

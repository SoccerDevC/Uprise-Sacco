<?php

namespace App\Http\Controllers;
use App\Models\Failed_Login;

use Illuminate\Http\Request;

class ReferenceController extends Controller
{
    public function update(Request $request)
{
    $referenceData = $request->input('message');
    
    // Loop through the submitted data and update the database accordingly
    foreach ($referenceData as $referenceNumber => $message) {
        $referenceRequest = Failed_Login::find($referenceNumber);
        if ($referenceRequest) {
            // Assuming the field name for the message column is 'message'
            $referenceRequest->message = $message;
            $referenceRequest->save();
        }
    }

    // Redirect back to the previous page or any other appropriate page
    return redirect()->back()->with('success', 'Messages updated successfully.');
}

    
}

<?php
namespace App\Http\Controllers;

use Illuminate\Http\Request;
use League\Csv\Reader;
use App\Models\Deposits;

class UploadController extends Controller
{
    public function upload(Request $request)
    {
        $this->uploadFile($request);

        $uploadedFile = $request->file('csv_file');
        // $uploadedFile->store('CSV'); // Specify the directory where the file should be stored
        // Additional processing or validation logic can be added here

        $contents = $uploadedFile->get();

        $reader = Reader::createFromString($contents);
        $rows = $reader->getRecords();

        $message = session('success', null); // Get the success message from the session

        return view('display', ['rows' => $rows, 'message' => $message]);
    }

    public function uploadFile(Request $request)
    {
        if ($request->hasFile('csv_file')) {
            $file = $request->file('csv_file');
            $filePath = $file->getRealPath();
            $data = array_map('str_getcsv', file($filePath));

            foreach ($data as $row) {
                // Assuming the CSV file has columns in the order: receipt_no, member_id, amount, date
                $deposit = new Deposits([
                    'receipt_number' => $row[0],
                    'member_id' => $row[1],
                    'amount' => $row[2],
                    'date' => $row[3],
                ]);
                $deposit->save();
            }
            session()->flash('success', 'CSV file data uploaded successfully.'); // Store the success message in the session
        }

        return redirect()->back();
    }
}

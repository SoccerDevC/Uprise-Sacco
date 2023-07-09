<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use League\Csv\Reader;

class UploadController extends Controller
{

    public function upload(Request $request)
    {
        $uploadedFile = $request->file('csv_file');
        // $uploadedFile->store('CSV'); // Specify the directory where the file should be stored
        // Additional processing or validation logic can be added here

        $contents = $uploadedFile->get();

        $reader = Reader::createFromString($contents);
        $rows = $reader->getRecords();
        
        return view('display', ['rows' => $rows]);
    }
}
